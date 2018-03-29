package io.virtdata.basicsmappers.from_long.to_string;

import io.virtdata.annotations.ThreadSafeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.LongFunction;
import java.util.regex.Pattern;

@ThreadSafeMapper
public class DirectoryLines implements LongFunction<String> {

    private final static Logger logger = LoggerFactory.getLogger(DirectoryLines.class);
    private final Pattern namePattern;
    private final String basepath;
    private final List<Path> allFiles;
    private Iterator<String> stringIterator;
    private Iterator<Path> pathIterator;

    public DirectoryLines(String basepath, String namePattern) {
        this.basepath = basepath;
        this.namePattern = Pattern.compile(namePattern);
        allFiles = getAllFiles();
        if (allFiles.size() == 0) {
            throw new RuntimeException("Loaded zero files from " + basepath + ", full path:" + Paths.get(basepath).getFileName());
        }
        pathIterator = allFiles.iterator();
        try {
            stringIterator = Files.readAllLines(pathIterator.next()).iterator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized String apply(long value) {
        while (!stringIterator.hasNext()) {
            if (pathIterator.hasNext()) {
                Path nextPath = pathIterator.next();
                try {
                    stringIterator = Files.readAllLines(nextPath).iterator();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                logger.debug("Resetting path iterator after exhausting input.");
                pathIterator = allFiles.iterator();
            }
        }
        return stringIterator.next();
    }

    private List<Path> getAllFiles() {
        logger.debug("Loading file paths from " + basepath);
        Set<FileVisitOption> options = new HashSet<>();
        options.add(FileVisitOption.FOLLOW_LINKS);
        FileList fileList = new FileList(namePattern);

        try {
            Files.walkFileTree(Paths.get(basepath), options, 10, fileList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("File reader: " + fileList.toString() + " in path: " + Paths.get(basepath).getFileName());
        fileList.paths.sort(Path::compareTo);
        return fileList.paths;
    }

    private static class FileList implements FileVisitor<Path> {
        public final Pattern namePattern;
        public int seen;
        public int kept;
        public List<Path> paths = new ArrayList<>();

        private FileList(Pattern namePattern) {
            this.namePattern = namePattern;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            seen++;
            if (file.toString().matches(namePattern.pattern())) {
                paths.add(file);
                kept++;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            logger.warn("Error traversing file: " + file + ":" + exc);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        public String toString() {
            return "" + kept + "/" + seen + " files with pattern '" + namePattern + "'";
        }

    }
}

