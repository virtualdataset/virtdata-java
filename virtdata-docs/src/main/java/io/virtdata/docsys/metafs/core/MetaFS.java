package io.virtdata.docsys.metafs.core;

import io.virtdata.docsys.metafs.fs.renderfs.fs.RenderedFileChannel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class MetaFS extends FileSystem {

    protected boolean isReadOnly=true;

    @Override
    public void close() throws IOException {
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isReadOnly() {
        return isReadOnly;
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        return null;
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        return null;
    }

    @Override
    public WatchService newWatchService() throws IOException {
        return null;
    }

    public Path getRootPath() {
        return new MetaPath(this, "/");
    }

    @Override
    public Path getPath(String first, String... more) {
        return new MetaPath(this, first, more);
    }

    /**
     * This should be implemented per filesystem, using the signature of
     * {@link java.nio.file.spi.FileSystemProvider#checkAccess(Path, AccessMode[])}
     */
    public void checkAccess(Path path, AccessMode[] modes) throws IOException {
        throw new RuntimeException("Implement me (checkAccess) for " + path.getFileSystem().toString());
    }

    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs)
            throws IOException {
        SeekableByteChannel seekableByteChannel = this.newByteChannel(path, options, attrs);
        return new RenderedFileChannel(seekableByteChannel);
    }

    public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
        HashSet optionSet = new HashSet<>(Arrays.asList(options));
        SeekableByteChannel seekableByteChannel = newByteChannel(path, optionSet);
        InputStream inputStream = Channels.newInputStream(seekableByteChannel);
        return inputStream;
    }

    public abstract SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException;

//    public abstract FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>[] attrs);

//    protected abstract BasicFileAttributes getFileAttributes(Path path, Class type, LinkOption[] options) throws IOException;
//
//    protected abstract Map<String, Object> getFileAttributes(Path path, String attributes, LinkOption[] options) throws IOException;
//
//    protected abstract FileAttributeView getFileAttributeView(Path path, Class type, LinkOption... options);
}
