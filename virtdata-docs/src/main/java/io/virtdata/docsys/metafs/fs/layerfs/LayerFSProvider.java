package io.virtdata.docsys.metafs.fs.layerfs;

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
import java.nio.file.spi.FileSystemProvider;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class LayerFSProvider extends MetaFSProvider {

    private static LayerFSProvider instance;
    private final Logger logger = LoggerFactory.getLogger(LayerFSProvider.class);

    private LayerFSProvider() {
    }

    public synchronized static LayerFSProvider get() {
        if (instance == null) {
            instance = new LayerFSProvider();
        }
        return instance;
    }

    @Override
    public synchronized FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        boolean readonly = true;
        LayerFS fs = new LayerFS(uri.toString());
        fs.setWritable(env != null && env.get("writable").toString().equals("true"));
        return fs;
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        MetaPath metapath = assertMetaPath(path);
        LayerFS layerFS = assertLayerFS(metapath);
        return layerFS.newByteChannel(path, options, attrs);
    }

//    @Override
//    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
//        MetaPath metapath = assertMetaPath(path);
//        LayerFS layerFS = assertLayerFS(metapath);
//        return layerFS.newFileChannel(path, options, attrs);
//    }


    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        MetaPath metapath = assertMetaPath(dir);
        LayerFS layerFS = assertLayerFS(metapath);

        Set<String> names = new HashSet<>();
        Set<Path> paths = new HashSet<>();
        IOException possibleException = null;
        int foundDirectoryCount = 0;
        for (FileSystem fs : layerFS.getWrappedFilesystems()) {
            Path fsSpecificPath = fs.getPath(metapath.toString());
            try {
                DirectoryStream<Path> dsp = fs.provider().newDirectoryStream(fsSpecificPath, filter);
                foundDirectoryCount++;
                for (Path path : dsp) {
                    if (!names.contains(path.toString())) {
                        names.add(path.toString());
                        paths.add(path);
                    }
                }
            } catch (IOException ioe) {
                possibleException = ioe;
            }
        }
        if (foundDirectoryCount == 0) {
            throw new IOException("Unable to find even one directory entry in addLayers for path " + dir);
        }

        return new DirectoryStream<Path>() {
            @Override
            public Iterator<Path> iterator() {
                return paths.iterator();
            }

            @Override
            public void close() throws IOException {
            }
        };
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        return acceptFirstSuccess(path, p -> {
            try {
                p.getFileSystem().provider().readAttributes(p,BasicFileAttributes.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return p.getFileSystem().provider().getFileAttributeView(p, type, options);
        });
    }


    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        return acceptFirstSuccess(path, p -> {
            try {
                FileSystem fs = p.getFileSystem();
                FileSystemProvider provider = fs.provider();
                A a = provider.readAttributes(p, type, options);
                return a;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        return acceptFirstSuccess(path, p -> {
            try {
                FileSystem fileSystem = p.getFileSystem();
                FileSystemProvider provider = fileSystem.provider();
                Map<String, Object> map = provider.readAttributes(p, attributes, options);
                return map;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private <T> T acceptFirstSuccess(Path path, Function<Path, T> process) {
        MetaPath metapath = assertMetaPath(path);
        LayerFS layerFS = assertLayerFS(metapath);
        List<Exception> exceptions = new ArrayList<>();
        for (FileSystem wrappedFilesystem : layerFS.getWrappedFilesystems()) {
            try {
                Path localizedpath = wrappedFilesystem.getPath(metapath.toString());
                return process.apply(localizedpath);
            } catch (Exception e) {
                exceptions.add(e);
            }
        }
        throw new RuntimeException(exceptions.size() + " exceptions occurred:" + exceptions.stream().map(Exception::getMessage).collect(Collectors.joining(",\n","\n","")));
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        MetaPath metapath = assertMetaPath(path);
        LayerFS layerFS = assertLayerFS(metapath);
        layerFS.checkAccess(path, modes);
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
                logger.trace("Did not find readable file " + toRead + " in fs " + fileSystem);
            }
        }
        throw new RuntimeException("Unable to find a readable " + toRead + " in any addLayer");
    }

    private LayerFS assertLayerFS(MetaPath path) {
        if (!(path.getFileSystem() instanceof LayerFS)) {

            throw new RuntimeException("Unable to do LayerFS operations on Path from filesystem of type " + path.getFileSystem().getClass().getCanonicalName());
        }
        return (LayerFS) path.getFileSystem();
    }

    private MetaPath assertMetaPath(Path path) {
        if (!(path instanceof MetaPath)) {
            throw new InvalidParameterException("Unable to do MetaPath operations on Path of type " + path.getClass().getCanonicalName());
        }
        return (MetaPath) path;
    }
}
