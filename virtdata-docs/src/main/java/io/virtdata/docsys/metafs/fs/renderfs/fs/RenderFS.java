package io.virtdata.docsys.metafs.fs.renderfs.fs;

import io.virtdata.docsys.metafs.core.MetaPath;
import io.virtdata.docsys.metafs.fs.renderfs.api.FileContentRenderer;
import io.virtdata.docsys.metafs.fs.renderfs.api.Renderers;
import io.virtdata.docsys.metafs.fs.virtual.VirtFS;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Set;

/**
 * The RenderFS filesystem will pretend that a rendered form
 * of some file types already exist in the filesystem, so long
 * as the necessary input file type and rendererTypes are present.
 * Directory listings, file access, and everything else that
 * would normally work for these files will work. RenderFS
 * will create in-memory versions of these files as needed.
 *
 * The rendered version of files automatically take on all
 * the attributes of their upstream file format, except
 * for file size and file data.
 */
public class RenderFS extends VirtFS {

    private final RenderFSProvider provider = RenderFSProvider.get();
    Renderers renderers = new Renderers();


    public RenderFS(FileSystem layer, String name) {
        super(layer.getPath(layer.getSeparator()), name);
    }

    public RenderFS(Path wrapped, String name) {
        super(wrapped, name);
    }

    public RenderFS(URI baseUri, String name) {
        this(Path.of(baseUri), name);
    }

    public Renderers getRenderers() {
        return renderers;
    }

    public void addRenderers(FileContentRenderer... rendererType) {
        for (FileContentRenderer renderer : rendererType) {
            renderers.add(renderer);
        }
    }

    @Override
    public RenderFSProvider provider() {
        return provider;
    }


    public DirectoryStream<Path> newDirectoryStream(DirectoryStream<Path> paths) {
        return new RenderFSDirectoryStream(paths, renderers);
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        MetaPath metaPath = assertMetaPath(path);
        Path syspath = this.metaToSysFunc.apply(metaPath);

        try {
            SeekableByteChannel channel = super.newByteChannel(path, options, attrs);
            return channel;
        } catch (Exception e) {
            FileContentRenderer renderer = renderers.forTargetPath(path);
            if (renderer != null) {
                return renderer.getByteChannel(path);
            }
        }
        return syspath.getFileSystem().provider().newByteChannel(syspath, options, attrs);
    }


    public BasicFileAttributes readAttributes(Path path, Class type, LinkOption... options) throws IOException {
        try {
            return super.readAttributes(path, type, options);
        } catch (Exception e1) {
            FileContentRenderer renderer = renderers.forTargetPath(path);
            if (renderer != null) {
                try {
                    Path sourcePath = renderer.getSourcePath(path);
                    ByteBuffer rendered = renderer.getRendered(path);
                    BasicFileAttributes attrs = readAttributes(sourcePath, type, options);
                    return new RenderedBasicFileAttributes(attrs, rendered.remaining());
                } catch (Exception e) {
                    throw new RuntimeException("There was a problem rendering " + path.toString() + " with " + renderer);
                }
            } else {
                throw e1;
            }
        }
    }

    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption[] options) throws IOException {
        try {
            return super.readAttributes(path, attributes, options);
        } catch (Exception e1) {
            FileContentRenderer renderer = renderers.forTargetPath(path);
            if (renderer != null) {
                Path sourcePath = renderer.getSourcePath(path);
                ByteBuffer rendered = renderer.getRendered(path);
                Map<String, Object> sourceAttrs = readAttributes(sourcePath, attributes, options);
                return new RenderedFileAttributeMap(sourcePath, sourceAttrs, path, rendered.remaining());
            } else {
                throw e1;
            }
        }
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        try {
            super.checkAccess(path, modes);
        } catch (Exception e1) {
            FileContentRenderer renderer = renderers.forTargetPath(path);
            if (renderer == null) {
                throw e1;
            }
        }
    }

    public FileAttributeView getFileAttributeView(Path path, Class type, LinkOption[] options) {

        /*
          Since an attribute view does not check to see if a file exists on the system first,
          and this method must return data about the same logical file as the #readAttributes
          method, we must first call #readAttributes, and then return a view of the original
          file IF and ONLY IF it exists. This assumes read-only semantics.
         */
        try {
            super.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
            return super.getFileAttributeView(path, type, options);
        } catch (IOException e1) {
            FileContentRenderer renderer = renderers.forTargetPath(path);
            if (renderer != null) {
                Path sourcePath = renderer.getSourcePath(path);
                ByteBuffer rendered = renderer.getRendered(path);
                FileAttributeView sourceFileAttributeView = getFileAttributeView(sourcePath, type, options);
                return new RenderedFileAttributeView(
                        sourcePath, sourceFileAttributeView, path, type, options, rendered.remaining()
                );
            } else {
                throw new RuntimeException(e1);
            }
        }


//        /*
//        Although the case for neither the source file or the target file being present is
//        actually an undefined case (Due to #readAttributes thrown an exception), we don't want
//        to cause callers who would get a view but who wouldn't trip over an error to be
//        forced to deal with one here. (Callers may get a view without getting the attributes.)
//         */
//        return super.getFileAttributeView(path, type, options);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return "RenderFS(" + getName() +"): root=" + super.getOuterMount().toString() + " renderers=" + renderers;
    }


    private VirtFS assertThisFs(MetaPath metaPath) {
        if (!(metaPath.getFileSystem() == this)) {
            throw new InvalidParameterException("This path is not a member of this filesystem.");
        }
        return (RenderFS) metaPath.getFileSystem();
    }

    private MetaPath assertMetaPath(Path path) {
        if (!(path instanceof MetaPath)) {
            throw new InvalidParameterException("This path must be an instance of MetaPath to work with " + this);
        }
        return (MetaPath) path;
    }

}
