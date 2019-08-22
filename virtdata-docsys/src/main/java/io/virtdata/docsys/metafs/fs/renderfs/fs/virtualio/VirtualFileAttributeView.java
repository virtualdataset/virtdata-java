package io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.function.LongSupplier;

public class VirtualFileAttributeView implements BasicFileAttributeView {
    private final FileAttributeView sourceAttributeView;
    private final Class type;
    private final LinkOption[] options;
    private final LongSupplier sizereader;
    private LongSupplier versionreader;
    private final Path targetPath;
    private final Path sourcePath;

    public VirtualFileAttributeView(
            Path sourcePath,
            FileAttributeView sourceAttributeView,
            Path targetPath,
            Class type,
            LinkOption[] options,
            LongSupplier sizereader,
            LongSupplier versionreader
    ) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.sourceAttributeView = sourceAttributeView;
        this.type = type;
        this.options = options;
        this.sizereader = sizereader;
        this.versionreader = versionreader;
    }

    @Override
    public String name() {
        return "rendered";
    }


//    @Override
//    public BasicFileAttributes readAttributes() throws IOException {
//        return );
//    }

    public VirtualFileBasicFileAttributes readAttributes() throws IOException {
        BasicFileAttributes delegateAttributes =
                sourcePath.getFileSystem().provider().readAttributes(targetPath, BasicFileAttributes.class);
        return new VirtualFileBasicFileAttributes(
                delegateAttributes,
                sizereader, versionreader);
    }
    @Override
    public void setTimes(
            FileTime lastModifiedTime,
            FileTime lastAccessTime,
            FileTime createTime
    ) throws IOException {
        throw new RuntimeException("This method is not supported in this implementation of RenderFS");
    }


}
