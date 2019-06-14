package io.virtdata.docsys.metafs.core;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Function;

public class PathTransformingDirectoryStream implements DirectoryStream<Path> {

    private final DirectoryStream<Path> sysdirstream;
    private final Function<Path, MetaPath> sysToMetaFunc;

    public PathTransformingDirectoryStream(DirectoryStream<Path> sysdirstream, Function<Path, MetaPath> sysToMetaFunc) {
        this.sysdirstream = sysdirstream;
        this.sysToMetaFunc = sysToMetaFunc;
    }

    @Override
    public Iterator<Path> iterator() {
        return new TransformingIterator<Path,Path>(sysToMetaFunc,sysdirstream.iterator());
    }

    @Override
    public void close() throws IOException {
        sysdirstream.close();
    }
}
