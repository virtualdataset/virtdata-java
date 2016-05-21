package com.metawiring.gen.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileReaders {
    private static final Logger logger = LoggerFactory.getLogger(FileReaders.class);

    public static List<String> loadToStringList(String resource) {
        CharBuffer charBuffer = loadFileToCharBuffer(resource);
        String[] split = charBuffer.toString().split("\\r*\\n");
        List<String> strings = Arrays.asList(split);
        return Collections.unmodifiableList(strings);
    }

    public static CharBuffer loadFileToCharBuffer(String filename, String... searchPaths) {

        InputStream stream = mapInputResource(filename, searchPaths);

        CharBuffer linesImage;
        try {
            InputStreamReader isr = new InputStreamReader(stream);
            linesImage = CharBuffer.allocate(1024 * 1024);
            while (isr.read(linesImage)>0) {
            }
            isr.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        linesImage.flip();
        return linesImage.asReadOnlyBuffer();
    }

    private static InputStream mapInputResource(String name, String... searchPaths) {
        InputStream stream = null;

        for (String searchPath : searchPaths) {
            File onFileSystem = new File( searchPath + File.separator + name);

            if (onFileSystem.exists()) {
                try {
                    stream = new FileInputStream(onFileSystem);
                    logger.debug("Found resource on filesystem:" + onFileSystem.getPath());
                    return stream;
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Unable to find file " + onFileSystem.getPath() + " after verifying that it exists.");
                }
            }
        }

        ClassLoader cl = FileReaders.class.getClassLoader();

        stream = cl.getResourceAsStream(name);
        if (stream!=null) {
            logger.debug("Found resource in class path:" + name);
            return stream;
        }

        for (String searchPath : searchPaths) {
            String inClassPath = searchPath + File.separator + name;
            stream = cl.getResourceAsStream(inClassPath);
            if (stream!=null) {
                logger.debug("Found resource in class path:" + inClassPath);
                return stream;
            }
        }

        throw new RuntimeException(name + " was missing from filesystem and classpath, with search paths:" +
                Arrays.asList(searchPaths));
    }

}
