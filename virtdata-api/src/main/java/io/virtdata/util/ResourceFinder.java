/*
 *
 *    Copyright 2016 jshook
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package io.virtdata.util;

import io.virtdata.services.ModuleDataService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The ResourceFinder class is the central file IO control point for all VirtData functions. VirtData mapping
 * functions may ask for access to some content to use as seed or reference data. Centralizing the
 * mechanisms used to access any of this data allows for access to the following:
 * <OL>
 * <LI>URL provided content - If the path is a well-formed URI, then it is read as such. If it is not,
 * then this resource finder is skipped.</LI>
 * <LI>Filesystem content - Files in the relative path of users are searched, using the default search
 * path prefixes.</LI>
 * <LI>Module content - JPMS modules may contain content within a MODULE-DATA directory. This
 * is enabled via a service hook and SPI, since modules must own their own content. The default search
 * path prefixes are also used here.</LI>
 * <LI>Classpath content - Resources in the classpath are searched, using the default search path prefixes.</LI>
 * </OL>
 *
 * These are in a priority order which is not configurable.
 */
public class ResourceFinder {

    public final static String DATA_DIR = "data";
    private final static Logger logger = LoggerFactory.getLogger(ResourceFinder.class);

    public static CharBuffer readDataFileToCharBuffer(String basename) {
        return loadFileToCharBuffer(basename, DATA_DIR);
    }

    public static List<String> readDataFileLines(String basename) {
        return readFileLines(basename, DATA_DIR);
    }

    public static String readDataFileString(String basename) {
        return readFileString(basename, DATA_DIR);
    }

    public static CSVParser readFileCSV(String basename, String... searchPaths) {
        Reader reader = findRequiredReader(basename, "csv", searchPaths);
        CSVFormat format = CSVFormat.newFormat(',').withFirstRecordAsHeader();
        try {
            CSVParser parser = new CSVParser(reader, format);
            return parser;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<Reader> findOptionalReader(String basename, String extenion, String... searchPaths) {
        return findOptionalStream(basename, extenion, searchPaths)
                .map(InputStreamReader::new)
                .map(BufferedReader::new);
    }

    public static Reader findRequiredReader(String basename, String extension, String... searchPaths) {
        Optional<Reader> optionalReader = findOptionalReader(basename, extension, searchPaths);
        return optionalReader.orElseThrow(() -> new RuntimeException(
                "Unable to find " + basename + " with extension " + extension + " in file system or in classpath, with"
                        + " search paths: " + Arrays.stream(searchPaths).collect(Collectors.joining(","))
        ));
    }

    public static List<String> readFileLines(String basename, String... searchPaths) {
        InputStream requiredStreamOrFile = findRequiredStream(basename, "", DATA_DIR);
        try (BufferedReader buffer = new BufferedReader((new InputStreamReader(requiredStreamOrFile)))) {
            List<String> collected = buffer.lines().collect(Collectors.toList());
            return collected;
        } catch (IOException ioe) {
            throw new RuntimeException("Error while reading required file to string", ioe);
        }
    }

    public static String readFileString(String basename, String... searchPaths) {
        InputStream requiredStreamOrFile = findRequiredStream(basename, "", searchPaths);
        try (BufferedReader buffer = new BufferedReader((new InputStreamReader(requiredStreamOrFile)))) {
            String filedata = buffer.lines().collect(Collectors.joining("\n"));
            return filedata;
        } catch (IOException ioe) {
            throw new RuntimeException("Error while reading required file to string", ioe);
        }
    }

    public static CharBuffer loadFileToCharBuffer(String filename, String... searchPaths) {
        InputStream stream = findRequiredStream(filename, "", searchPaths);

        CharBuffer linesImage;
        try {
            InputStreamReader isr = new InputStreamReader(stream);
            linesImage = CharBuffer.allocate(1024 * 1024);
            while (isr.read(linesImage) > 0) {
            }
            isr.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        linesImage.flip();
        return linesImage.asReadOnlyBuffer();
    }

    public static InputStream findRequiredStream(String basename, String extension, String... searchPaths) {
        Optional<InputStream> optionalStreamOrFile = findOptionalStream(basename, extension, searchPaths);
        return optionalStreamOrFile.orElseThrow(() -> new RuntimeException(
                "Unable to find " + basename + " with extension " + extension + " in file system or in classpath, with"
                        + " search paths: " + Arrays.stream(searchPaths).collect(Collectors.joining(","))
        ));
    }

    public static Optional<InputStream> findOptionalStream(String basename, String extension, String... searchPaths) {

        boolean needsExtension = (extension != null && !extension.isEmpty() && !basename.endsWith("." + extension));
        String filename = basename + (needsExtension ? "." + extension : "");

        ArrayList<String> paths = new ArrayList<String>() {{
            add(filename);
            if (!isRemote(basename)) {
                addAll(Arrays.stream(searchPaths).map(s -> s + File.separator + filename)
                        .collect(Collectors.toCollection(ArrayList::new)));
            }
        }};

        for (String path : paths) {
            Optional<InputStream> stream = getInputStream(path);
            if (stream.isPresent()) {
                return stream;
            }
        }

        return Optional.empty();
    }

    public static Optional<InputStream> getInputStream(String path) {

        // URLs, if http: or https:
        if (isRemote(path)) {
            URL url;
            try {
                url = new URL(path);
                InputStream inputStream = url.openStream();
                if (inputStream != null) {
                    return Optional.of(inputStream);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // Files
        try {
            InputStream stream = new FileInputStream(path);
            return Optional.of(stream);
        } catch (FileNotFoundException ignored) {
        }

        // module data services
        try {
            ServiceLoader<ModuleDataService> dataLoaders = ServiceLoader.load(ModuleDataService.class);
            for (ModuleDataService dataLoader : dataLoaders) {
                Path candidatePath = Path.of(path);
                InputStream inputStream = dataLoader.getInputStream(candidatePath);
                if (inputStream != null) {
                    Optional<InputStream> optionalStream = Optional.of(inputStream);
                    return optionalStream;
                }
            }
        } catch (Exception ignored) {
            logger.trace("error using module data service for " + path.toString() + ": " + ignored.getMessage(), ignored);
        }

        // Classpath
        ClassLoader classLoader = ResourceFinder.class.getClassLoader();
        InputStream stream = classLoader.getResourceAsStream(path);
        if (stream != null) {
            return Optional.of(stream);
        }

        return Optional.empty();
    }

    private static boolean isRemote(String path) {
        return (path.toLowerCase().startsWith("http:")
                || path.toLowerCase().startsWith("https:"));
    }

}
