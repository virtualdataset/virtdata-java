package io.virtdata.docsys.metafs.fs.renderfs.fs;

import io.virtdata.docsys.metafs.core.MetaPath;
import io.virtdata.docsys.metafs.fs.virtual.VirtFSProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Set;

public class RenderFSProvider extends VirtFSProvider {

    protected static RenderFSProvider instance;

    public synchronized static RenderFSProvider get() {
        if (instance == null) {
            instance = new RenderFSProvider();
        }
        return instance;
    }

    @Override
    public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
        RenderFS renderFS = assertThisFS(path);
        return renderFS.newInputStream(path, options);

    }


    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        RenderFS renderFS = assertThisFS(path);
        Path syspath = getContainerPath(path);
        return renderFS.newByteChannel(path, options, attrs);
    }

    private RenderFS assertThisFS(Path path) {
        if (!(path instanceof MetaPath)) {
            throw new InvalidParameterException("This path must be a MetaPath");
        }
        MetaPath mp = (MetaPath) path;
        if (!(mp.getFileSystem() instanceof RenderFS)) {
            throw new InvalidParameterException("This metapath must be for a RenderFS");
        }

        return (RenderFS) mp.getFileSystem();
    }

    @Override
    public FileChannel newFileChannel(
            Path path,
            Set<? extends OpenOption> options,
            FileAttribute<?>... attrs) throws IOException {
        RenderFS renderFS = assertThisFS(path);
        Path syspath = getContainerPath(path);
        return renderFS.newFileChannel(path, options, attrs);
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        RenderFS renderFS = assertThisFS(dir);
        DirectoryStream<Path> paths = super.newDirectoryStream(dir, filter);
        return renderFS.newDirectoryStream(paths);
    }

    @Override
    public BasicFileAttributes readAttributes(Path path, Class type, LinkOption... options) throws IOException {
        RenderFS renderFS = assertThisFS(path);
        return renderFS.readAttributes(path, type, options);
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        RenderFS renderFS = assertThisFS(path);
         return renderFS.readAttributes(path, attributes, options);
   }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        RenderFS renderFS = assertThisFS(path);
        renderFS.checkAccess(path, modes);
    }

    @Override
    public FileAttributeView getFileAttributeView(Path path, Class type, LinkOption... options) {
        RenderFS renderFS = assertThisFS(path);
        return renderFS.getFileAttributeView(path, type, options);
    }
}
