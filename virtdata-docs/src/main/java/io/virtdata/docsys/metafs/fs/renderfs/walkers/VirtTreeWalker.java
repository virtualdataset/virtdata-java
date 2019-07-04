package io.virtdata.docsys.metafs.fs.renderfs.walkers;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.function.Predicate;

public class VirtTreeWalker {

    public static void walk(Path p, PathVisitor v, Predicate<String> filter) {
        try {
            FileSystemProvider provider = p.getFileSystem().provider();
            DirectoryStream<Path> paths = provider.newDirectoryStream(p, entry -> true);
            for (Path path : paths) {
                if (filter.test(path.toString())) {
                    v.visit(path);
//                } else {
//                    BasicFileAttributes basicFileAttributes = provider.readAttributes(path, BasicFileAttributes.class);
//                    if (basicFileAttributes.isDirectory()) {
//
//                    }
//                }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface PathVisitor {
        void visit(Path p);
    }

}
