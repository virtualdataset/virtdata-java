package io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.CachedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final static Logger logger = LoggerFactory.getLogger(VirtualFile.class);

    private final Path target;
    private final Path delegate;
    private final CachedContent<String> contents;

    public VirtualFile(Path delegate, Path target, RenderedContent<String> content) {
        this.delegate = delegate;
        this.target = target;
        this.contents = new CachedContent<>(target.toString(),content,content);
    }

    private ByteBuffer getContent() {
        logger.trace("ACCESSING CONTENT " + this.target.toString());
        String s = contents.get();
        ByteBuffer byteBuffer = ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8)).asReadOnlyBuffer();
        return byteBuffer;
    }

    public BasicFileAttributes readAttributes(
            Path path,
            Class<? extends BasicFileAttributes> type,
            LinkOption... options) throws IOException {
        FileSystemProvider provider = delegate.getFileSystem().provider();
        BasicFileAttributes delegateAttrs = provider.readAttributes(delegate, type, options);
        return new VirtualFileBasicFileAttributes(
                delegateAttrs, () -> getContent().remaining(),
                () -> contents.getVersion()
        );
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
        return new VirtualFileAttributeMap(
                delegate,
                sourceAttrs,
                path,
                () -> getContent().remaining(),
                contents::getVersion);
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
                delegate,
                sourceFileAttributeView,
                path,
                type,
                options,
                () -> getContent().remaining(),
                contents::getVersion
        );
    }

    public RenderedContent getRenderedContent() {
        return contents;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.target.toString());
        sb.append(":[").append(contents.toString()).append("]");

        return sb.toString();
    }

    public boolean isValid() {
        return getRenderedContent().isValid();
    }

}
