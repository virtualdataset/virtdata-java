package io.virtdata.docsys.metafs.fs.layerfs;

import io.virtdata.docsys.metafs.core.MetaFS;
import io.virtdata.docsys.metafs.core.MetaPath;
import io.virtdata.docsys.metafs.fs.virtual.VirtFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This filesystem is a filesystem aggregator, allowing users to
 * provide layered views of existing filesystems. The URI is merely
 * symbolic in this filesystem, since the user must endRenderers any wrapped
 * filesystems separately from the call to
 * {@link java.nio.file.spi.FileSystemProvider#newFileSystem(URI, Map)}.
 *
 * <H2>Meta Details</H2>
 *
 * <P>In general, operations are attempted on each filesystem, each
 * with a path scoped to that filesystem instance, until the operation
 * succeeds without throwing an error.
 * </P>
 *
 * <P>For operations which specify read-specific or write-specific options,
 * any read-only filesystems are skipped when write mode is requested.
 * By default, filesystems are registered as readonly, providing some
 * default safety.</P>
 */
@SuppressWarnings("ALL")
public class LayerFS extends MetaFS {

    private final String name;
    private final static Logger logger = LoggerFactory.getLogger(LayerFS.class);
    private static LayerFSProvider provider = LayerFSProvider.get();
    private List<FileSystem> wrappedFilesystems = new ArrayList<>();


    public LayerFS(String name) {
        this.name = name;
    }

    public LayerFS setWritable(boolean writable) {
        this.isReadOnly=!writable;
        return this;
    }

    public LayerFS addLayer(Path outerPath, String pathSymbolicName) {
        VirtFS metafs = new VirtFS(outerPath, pathSymbolicName);
        this.wrappedFilesystems.add(metafs);
        return this;
    }

    public LayerFS addLayer(FileSystem fileSystem) {
        this.wrappedFilesystems.add(fileSystem);
        return this;
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        Set<String> pathNames = new HashSet<>();
        Set<Path> paths = new HashSet<>();
        for (FileSystem fileSystem : wrappedFilesystems) {
            for (Path rootDirectory : fileSystem.getRootDirectories()) {
                String dirName = rootDirectory.toString();
                if (!pathNames.contains(dirName)) {
                    pathNames.add(dirName);
                    paths.add(rootDirectory);
                }
            }
        }
        return paths;
    }

    @Override
    public LayerFSProvider provider() {
        return provider;
    }

    @Override
    public String getSeparator() {
        if (wrappedFilesystems.size() > 0) {
            return wrappedFilesystems.get(0).getSeparator();
        }
        return FileSystems.getDefault().getSeparator();
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        return () -> wrappedFilesystems.stream().map(FileSystem::getFileStores)
                .map(f -> StreamSupport.stream(f.spliterator(), false))
                .flatMap(i -> StreamSupport.stream(i.spliterator(), false)).iterator();
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        if (wrappedFilesystems.size()==0) {
            return new HashSet<>();
        }
        Set<String> sfav = null;
        for (FileSystem fileSystem : wrappedFilesystems) {
            if (sfav == null) {
                sfav = fileSystem.supportedFileAttributeViews();
            } else {
                sfav.retainAll(fileSystem.supportedFileAttributeViews());
            }
        }
        return sfav;
    }

    @Override
    public Path getPath(String first, String... more) {
        return new MetaPath(this, first, more);
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        logger.debug("newByteChannel for " + path);
        MetaPath metapath = assertMetaPath(path);
        LayerFS layerFS = assertThisFs(metapath);
        try {

            if (options.contains(StandardOpenOption.READ) || options.isEmpty()) {
                Path firstReadablePath = findFirstReadablePath(metapath, layerFS.getWrappedFilesystems());
                return firstReadablePath.getFileSystem().provider().newByteChannel(firstReadablePath, options, attrs);
            } else {
                Path firstWritablePath = findFirstWritablePath(metapath, layerFS.getWrappedFilesystems());
                return firstWritablePath.getFileSystem().provider().newByteChannel(firstWritablePath, options, attrs);
            }
        } catch (FileNotFoundException fnfe) {
            throw fnfe;
        }

    }

    @Override
    public String getName() {
        return name;
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


    public List<FileSystem> getWrappedFilesystems() {
        return this.wrappedFilesystems;
    }

    @Override
    public String toString() {
        return "LayerFS(" + getName() + "):" + wrappedFilesystems.stream().map(String::valueOf).collect(Collectors.joining(",", "[[", "]]"));
    }

    private LayerFS assertThisFs(MetaPath path) {
        if (!(path.getFileSystem()==this)) {
            throw new RuntimeException("Unable to do LayerFS operations on Path from a different filesystem " + path.getFileSystem().getClass().getCanonicalName());
        }
        return (LayerFS) path.getFileSystem();
    }

    private MetaPath assertMetaPath(Path path) {
        if (!(path instanceof MetaPath)) {
            throw new InvalidParameterException("Unable to do MetaPath operations on Path of type " + path.getClass().getCanonicalName());
        }
        return (MetaPath) path;
    }

    private Path findFirstWritablePath(Path toWrite, List<FileSystem> fileSystems) {
        for (FileSystem fs : fileSystems) {
            if (!fs.isReadOnly()) {
                return fs.getPath(toWrite.toString());
            }
        }
        throw new RuntimeException("Unable to find a writable filesystem in addLayers.");

    }

    private Path findFirstReadablePath(Path toRead, List<FileSystem> fileSystems) {
        for (FileSystem fileSystem : fileSystems) {
            try {
                Path fsSpecificPath = fileSystem.getPath(toRead.toString());
                fsSpecificPath.getFileSystem().provider().checkAccess(fsSpecificPath, AccessMode.READ);
                return fsSpecificPath;
            } catch (IOException e) {
                logger.warn("Did not find readable file " + toRead + " in fs " + fileSystem);
            }
        }
        throw new RuntimeException("Unable to find a readable " + toRead + " in any addLayer");
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        MetaPath metapath = assertMetaPath(path);
        LayerFS layerFS = assertThisFs(metapath);

        IOException possibleException = null;
        for (FileSystem wrappedFilesystem : layerFS.getWrappedFilesystems()) {
            try {
                Path wrappedPath = wrappedFilesystem.getPath(metapath.toString());
                wrappedPath.getFileSystem().provider().checkAccess(wrappedPath, modes);
                return;
            } catch (IOException ioe) {
                possibleException = ioe;
            }
        }
        if (possibleException != null) {
            throw possibleException;
        }
        throw new RuntimeException("Invalid condition.");
    }


}
