package io.virtdata.docsys.metafs.fs.virtual;

import io.virtdata.docsys.metafs.core.MetaFSProvider;
import io.virtdata.docsys.metafs.core.MetaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.security.InvalidParameterException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class VirtFSProvider extends MetaFSProvider {

    protected final static Logger logger = LoggerFactory.getLogger(VirtFSProvider.class);
    private static VirtFSProvider instance;
    protected Map<URI, VirtFS> filesystems = new LinkedHashMap<>();

    protected VirtFSProvider() {}
    public synchronized static VirtFSProvider get() {
        if (instance==null) {
            instance = new VirtFSProvider();
        }
        return instance;
    }

    @Override
    public String getScheme() {
        return "meta";
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        if (filesystems.containsKey(uri)) {
            throw new FileSystemAlreadyExistsException("meta FileSystem under URI " + uri + " already exists.");
        }
        VirtFS virtFS = new VirtFS(Path.of(uri), uri.toString());
        filesystems.put(uri, virtFS);
        return virtFS;
    }

    @Override
    public synchronized FileSystem getFileSystem(URI uri) {
        VirtFS virtFS = filesystems.get(uri);
        if (virtFS ==null) {
            throw new FileSystemNotFoundException("A meta FileSystem was not found for URI " + uri);
        }
        return virtFS;
    }

    /**
     * {@inheritDoc}
     *
     * <H2>Meta Details</H2>
     * <P>
     * Although this is a meta FS, and the URIs are essentially symbolic, we still enforce that
     * the logical nesting of a filesystem URI and a path URI will determine when this method
     * will return a valid value.
     *
     * In other words, a path will not be returned for a meta file system wherein the URI of that
     * file system does not logical contain the inner path.
     *
     * This should maintain important semantics about usage and filtering that developers may
     * take for granted.
     * </P>
     */
    @Override
    public Path getPath(URI uri) {
        if (!uri.getScheme().equals(this.getScheme())) {
            throw new IllegalArgumentException("Invalid uri scheme '" + uri.getScheme() + "' for " + this.getClass().getCanonicalName() + ".getPath(URI)");
        }
        for (Map.Entry<URI, VirtFS> entry : filesystems.entrySet()) {
            if (entry.getKey().toString().startsWith(uri.toString())) {
                URI relativeUri = entry.getKey().relativize(uri);
                return Path.of(relativeUri);
            }
        }
        throw new FileSystemNotFoundException("A file system for provider-scoped URI '" + uri + "' was not found.");
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        MetaPath metaPath= assertMetaPath(path);
        return metaPath.getFileSystem().newByteChannel(path, options, attrs);
//        Path sysPath = getContainerPath(metapath);
//        return sysPath.getFileSystem().provider().newByteChannel(sysPath,options,attrs);
    }


    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {

    }

    @Override
    public void delete(Path path) throws IOException {

    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {

    }

    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {

    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        return false;
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        return false;
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        return null;
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        Path sysPath = getContainerPath(path);
        sysPath.getFileSystem().provider().checkAccess(sysPath,modes);
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        Path sysPath = getContainerPath(path);
        V fileAttributeView = sysPath.getFileSystem().provider().getFileAttributeView(sysPath, type, options);
        return fileAttributeView;
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        Path syspath = getContainerPath(path);
        A attributes = syspath.getFileSystem().provider().readAttributes(syspath, type, options);
        return attributes;
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        Path syspath = getContainerPath(path);
        Map<String, Object> stringObjectMap = syspath.getFileSystem().provider().readAttributes(syspath, attributes, options);
        return stringObjectMap;
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {
        Path syspath = getContainerPath(path);
        syspath.getFileSystem().provider().setAttribute(syspath, attribute, value, options);
    }

    protected Path getContainerPath(Path path) {
        MetaPath metapath = assertMetaPath(path);
        VirtFS virtfs = assertVirtFS(metapath);
        return virtfs.metaToSysFunc.apply(metapath);
    }

    private VirtFS assertVirtFS(MetaPath metaPath) {
        if (!(metaPath.getFileSystem() instanceof VirtFS)) {
            throw new InvalidParameterException("This path is not a member of a VirtFS filesystem.");
        }
        return (VirtFS) metaPath.getFileSystem();
    }

    private MetaPath assertMetaPath(Path path) {
        if (!(path instanceof MetaPath)) {
            throw new InvalidParameterException("This path must be an instance of MetaPath to work with " + this);
        }
        return (MetaPath)path;
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        MetaPath metapath= assertMetaPath(dir);
        VirtFS virtFS = assertVirtFS(metapath);
        return virtFS.newDirectoryStream(metapath, filter);
    }
}
