package io.virtdata.docsys.metafs.fs.renderfs.walkers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.List;

public class VirtTreeWalker {
    private final static Logger logger = LoggerFactory.getLogger(VirtTreeWalker.class);

    public static void walk(Path p, PathVisitor v, DirectoryStream.Filter<Path> filter) {
        try {
            FileSystemProvider provider = p.getFileSystem().provider();
            DirectoryStream<Path> paths = provider.newDirectoryStream(p, (Path r) -> true);
            List<Path> pathlist = new ArrayList<>();
            for (Path path : paths) {
                pathlist.add(path);
            }
            for (Path path : pathlist) {
                if (filter.accept(path)) {
                    logger.info("COMPUTE TOPICS for(" + path + ")");
                    v.visit(path);
                } else if (path.getFileSystem().provider().readAttributes(path, BasicFileAttributes.class).isDirectory()) {
                    // TODO: fix recursion here that results from cyclic dependency
                    logger.info("WALK TOPICS in " + path);
                    walk(path, v, filter);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface PathVisitor {
        void visit(Path p);
    }

//    private final static class nameFilter implements DirectoryStream.Filter<Path> {
//        private final Pattern namePattern;
//
//        public nameFilter(Pattern namePattern) {
//            this.namePattern = namePattern;
//        }
//
//        @Override
//        public boolean accept(Path entry) throws IOException {
//            if (namePattern.matcher(entry.toString()).matches()) {
//                return true;
//            }
//            if (entry.toString().matches(namePattern.toString());
//            return false;
//        }
//    }
}
