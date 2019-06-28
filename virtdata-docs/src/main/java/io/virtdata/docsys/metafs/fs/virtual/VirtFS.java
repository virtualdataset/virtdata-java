package io.virtdata.docsys.metafs.fs.virtual;

import io.virtdata.docsys.metafs.core.MetaFS;
import io.virtdata.docsys.metafs.core.MetaPath;
import io.virtdata.docsys.metafs.core.PathTransformingDirectoryStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * This MetaFS filesystem type simply virtualizes a root directory
 * behind a filesystem interface.
 */
public class VirtFS extends MetaFS {

    protected final static Logger logger = LoggerFactory.getLogger(VirtFS.class);

    protected final VirtFSProvider provider;

    private final Path outerMount;
    private final Path innerRoot = new MetaPath(this, "/");
    public final Function<MetaPath, Path> metaToSysFunc;
    public final Function<Path, MetaPath> sysToMetaFunc;
    private Path sysDefaultPath;
    private final String name;


//    protected VirtFS(VirtFSProvider provider, URI uri) {
//        this.provider = provider;
//
//        outerMount=new URI("VIRTUAL",null,uri.getTargetPath(),null, null);
//        this.uri = uri;
//        this.innerRoot = new MetaPath(this,"/");
//    }

//    public VirtFS(Path outerPath, Path sysDefaultPath) {
//        this.outerMount = outerPath;
//        provider = VirtFSProvider.get();
//        setSysDefaultPath(sysDefaultPath);
//        metaToSysFunc = new MapMetaPathToContainerPath(outerMount, this);
//        sysToMetaFunc = new MapContainerPathToMetaPath(outerMount, this);
//    }
//

    public VirtFS(Path outerPath, String name) {
        this.name = name;
        this.outerMount = outerPath;
        provider = VirtFSProvider.get();
        setSysDefaultPath(outerPath);
        metaToSysFunc = new MapMetaPathToContainerPath(outerMount, this);
        sysToMetaFunc = new MapContainerPathToMetaPath(outerMount, this);

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        MetaPath metaPath = assertMetaPath(path);
        Path syspath = this.metaToSysFunc.apply(metaPath);
        return syspath.getFileSystem().provider().newByteChannel(syspath,options,attrs);
    }

    public VirtFS setSysDefaultPath(Path sysDefaultPath) {
        if (sysDefaultPath.getFileSystem() != outerMount.getFileSystem()) {
            throw new InvalidParameterException("The default path must be part of the enclosing filesystem.");
        }
        this.sysDefaultPath = sysDefaultPath.toAbsolutePath();
        return this;
    }

    @Override
    public FileSystemProvider provider() {
        return provider;
    }


    @Override
    public String getSeparator() {
        return outerMount.getFileSystem().getSeparator();
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        Iterable<Path> outerRoots = outerMount.getFileSystem().getRootDirectories();
        List<Path> localizedPaths = StreamSupport
                .stream(outerRoots.spliterator(), false)
                .map(p -> this.getPath(p.getName(0).toString()))
                .collect(Collectors.toList());
        return localizedPaths;
    }

    @Override
    public Iterable<FileStore> getFileStores() {
        return outerMount.getFileSystem().getFileStores();
    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        return outerMount.getFileSystem().supportedFileAttributeViews();
    }

    @Override
    public Path getPath(String first, String... more) {
        return new MetaPath(this, first, more);
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


    private MetaPath assertMetaPath(Path metaPath) {
        if (metaPath instanceof MetaPath) {
            return ((MetaPath) metaPath);
        }
        throw new InvalidParameterException("This path was expected to be of type MetaPath");
    }

    @Override
    public String toString() {
        return "VirtFS(" + getName()+ "):" + "[" + this.outerMount.toString() + " -> /]";
    }

    private static class MapContainerPathToMetaPath implements Function<Path, MetaPath> {
        private Path containerPath;
        private VirtFS filesystem;

        public MapContainerPathToMetaPath(Path containerPath, VirtFS filesystem) {
            this.containerPath = containerPath;
            this.filesystem = filesystem;
        }

        @Override
        public MetaPath apply(Path path) {
            if (containerPath.getFileSystem() != path.getFileSystem()) {
                throw new InvalidParameterException("Must be part of the same container filesystem");
            }
            Path relative = containerPath.relativize(path);
            return new MetaPath(filesystem, containerPath.getFileSystem().getSeparator() + relative.toString());
        }
    }

    private static class MapMetaPathToContainerPath implements Function<MetaPath, Path> {
        private Path containerPath;
        private VirtFS filesystem;

        public MapMetaPathToContainerPath(Path containerPath, VirtFS filesystem) {
            this.containerPath = containerPath;
            this.filesystem = filesystem;
        }

        @Override
        public Path apply(MetaPath metapath) {
            if (filesystem != metapath.getFileSystem()) {
                throw new InvalidParameterException("Must be part of the same meta filesystem");
            }
            if (metapath.isAbsolute()) {
                return containerPath.resolve(metapath.asRelativePath().toString());
            }
            throw new InvalidParameterException("The MetaPath must be absolute unless a default path is provided");
        }
    }

    protected BasicFileAttributes readAttributes(Path path, Class<BasicFileAttributes> type, LinkOption[] options) throws IOException {
        MetaPath metaPath = assertMetaPath(path);
        Path syspath = this.metaToSysFunc.apply(metaPath);
        return syspath.getFileSystem().provider().readAttributes(syspath,type,options);
    }

    protected Map<String, Object> readAttributes(Path path, String attributes, LinkOption[] options) throws IOException {
        MetaPath metaPath = assertMetaPath(path);
        Path syspath = this.metaToSysFunc.apply(metaPath);
        return syspath.getFileSystem().provider().readAttributes(syspath, attributes, options);
    }

    public FileAttributeView getFileAttributeView(Path path, Class type, LinkOption... options) {
        MetaPath metaPath = assertMetaPath(path);
        Path syspath = this.metaToSysFunc.apply(metaPath);
        return syspath.getFileSystem().provider().getFileAttributeView(syspath, type, options);
    }

    public DirectoryStream<Path> newDirectoryStream(MetaPath metapath, DirectoryStream.Filter<? super Path> filter) throws IOException {
        Path syspath = metaToSysFunc.apply(metapath);
        DirectoryStream<Path> sysdirstream = syspath.getFileSystem().provider().newDirectoryStream(syspath, filter);
        return new PathTransformingDirectoryStream(sysdirstream, sysToMetaFunc);
    }

    @Override
    public void checkAccess(Path path, AccessMode[] modes) throws IOException {
        MetaPath metaPath = assertMetaPath(path);
        Path syspath = this.metaToSysFunc.apply(metaPath);
        syspath.getFileSystem().provider().checkAccess(syspath, modes);
    }


    protected Path getOuterMount() {
        return outerMount;
    }

}
