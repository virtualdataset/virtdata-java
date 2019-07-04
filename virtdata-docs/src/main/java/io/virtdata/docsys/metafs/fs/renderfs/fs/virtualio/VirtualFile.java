package io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;

public class VirtualFile {

    private final Path target;
    private final Path delegate;
    private final RenderedContent content;

    public VirtualFile(Path delegate, Path target, RenderedContent content) {
        this.delegate = delegate;
        this.target = target;
        this.content = content;
    }

    private ByteBuffer getContent() {
        return ByteBuffer.wrap(content.get().getBytes(StandardCharsets.UTF_8)).asReadOnlyBuffer();
//        if (contentCache == null) {
//            contentCache = dynamicRenderer.get().asReadOnlyBuffer();
//        }
//        return contentCache;
    }

    public BasicFileAttributes readAttributes(
            Path path,
            Class<? extends BasicFileAttributes> type,
            LinkOption... options) throws IOException {
        FileSystemProvider provider = delegate.getFileSystem().provider();
        BasicFileAttributes delegateAttrs = provider.readAttributes(delegate, type, options);
        return new VirtualFileBasicFileAttributes(delegateAttrs, getContent().remaining());
    }

    public SeekableByteChannel getSeekableByteChannel() {
        try {
            SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
            channel.write(getContent().asReadOnlyBuffer());
            channel.position(0);
            return channel;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption[] options) throws IOException {
        FileSystemProvider provider = delegate.getFileSystem().provider();
        Map<String, Object> sourceAttrs = provider.readAttributes(delegate, attributes, options);
        return new VirtualFileAttributeMap(delegate, sourceAttrs, path, getContent().remaining());
    }

    public void checkAccess(Path path, AccessMode[] modes) throws IOException {
        for (AccessMode mode : modes) {
            if (mode == AccessMode.WRITE) {
                throw new AccessDeniedException(path.toString());
            }
            if (mode == AccessMode.EXECUTE) {
                throw new AccessDeniedException(path.toString());
            }
        }
    }

    public FileAttributeView getFileAttributeView(Path path, Class type, LinkOption[] options) {
        FileSystemProvider provider = delegate.getFileSystem().provider();
        FileAttributeView sourceFileAttributeView = provider.getFileAttributeView(delegate, type, options);
        return new VirtualFileAttributeView(
                delegate, sourceFileAttributeView, path, type, options, getContent().remaining()
        );
    }

    public RenderedContent getRenderedContent() {
        return content;
    }

}
