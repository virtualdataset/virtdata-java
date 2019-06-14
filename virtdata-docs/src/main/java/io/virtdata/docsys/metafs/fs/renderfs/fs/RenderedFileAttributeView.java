package io.virtdata.docsys.metafs.fs.renderfs.fs;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;

public class RenderedFileAttributeView implements BasicFileAttributeView {
    private final FileAttributeView sourceAttributeView;
    private final long size;
    private final Path targetPath;
    private final Path sourcePath;

    public RenderedFileAttributeView(Path sourcePath, FileAttributeView sourceAttributeView,
                                     Path targetPath, Class type, LinkOption[] options, long size) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.sourceAttributeView = sourceAttributeView;
        this.size = size;
    }

    @Override
    public String name() {
        return "rendered";
    }

    @Override
    public BasicFileAttributes readAttributes() throws IOException {
        return targetPath.getFileSystem().provider().readAttributes(targetPath,BasicFileAttributes.class);
    }

    @Override
    public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
        throw new RuntimeException("This method is not supported in this implementation of RenderFS");
    }


}
