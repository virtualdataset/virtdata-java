package io.virtdata.docsys.metafs.fs.renderfs.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;

@SuppressWarnings("Duplicates")

public class RendererIO {
    public final static Logger logger = LoggerFactory.getLogger(RendererIO.class);

    public static String readString(Path path) {
        try {
            InputStream inputStream = path.getFileSystem().provider().newInputStream(path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            inputStream.transferTo(bos);
            return new String(bos.toByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static ByteBuffer readBuffer(Path path) {
        try {
            InputStream inputStream = path.getFileSystem().provider().newInputStream(path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            inputStream.transferTo(bos);
            return ByteBuffer.wrap(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BasicFileAttributes getFileAttributes(Path path) {
        try {
            BasicFileAttributeView fileAttributeView = path.getFileSystem().provider().getFileAttributeView(path, BasicFileAttributeView.class);
            return fileAttributeView.readAttributes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static long mtimeFor(Path path) {
        logger.trace("MTIME FOR " + path);
        return getFileAttributes(path).lastModifiedTime().toMillis();
    }

    public static Function<Path,ByteBuffer> PATH_BUFFER_FUNCTION = RendererIO::readBuffer;
}
