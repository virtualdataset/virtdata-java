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

//    public static final ThreadLocal<Set<Path>> TLPATHS = ThreadLocal.withInitial(HashSet::new);

    private final static Logger logger = LoggerFactory.getLogger(VirtTreeWalker.class);

    public static void walk(Path p, PathVisitor v, DirectoryStream.Filter<Path> filter) {
        try {
            logger.trace(" TOPIC-WALK > " + p);
            FileSystemProvider provider = p.getFileSystem().provider();
            DirectoryStream<Path> paths = provider.newDirectoryStream(p, (Path r) -> true);
            List<Path> pathlist = new ArrayList<>();
            for (Path path : paths) {
                pathlist.add(path);
            }
            for (Path path : pathlist) {
                if (path.getFileSystem().provider().readAttributes(path, BasicFileAttributes.class).isDirectory()) {
                    v.preVisitDir(path);
                    walk(path, v, filter);
                    v.postVisitDir(path);
                } else if (filter.accept(path)) {
//                    if (TLPATHS.get().contains(path)) {
//                        logger.warn("extra recursion");
//                    }
//                    TLPATHS.get().add(path);
                    logger.trace("----> COMPUTE TOPICS for (" + path + ")");
                    v.preVisitFile(path);
                    v.visit(path);
                    v.postVisitFile(path);
                    logger.trace("<--- END COMPUTE TOPICS  (" + path + ")");

                } else {
                    //logger.error("WHOOPS");
                }
            }
            logger.trace(" TOPIC-WALK < " + p);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface PathVisitor {
        void visit(Path p);
        default void preVisitFile(Path path) {}
        default void postVisitFile(Path path) {}
        default void preVisitDir(Path path) {}
        default void postVisitDir(Path path) {}
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
