package io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public class VirtualFileBasicFileAttributes implements BasicFileAttributes {

    private BasicFileAttributes sysFileAttributeDelegate;
    private LongSupplier sizereader;
    private LongSupplier versionreader;

    public VirtualFileBasicFileAttributes(
            BasicFileAttributes sysFileAttributeDelegate, LongSupplier sizereader, LongSupplier versionreader) {
        this.sysFileAttributeDelegate = sysFileAttributeDelegate;
        this.sizereader = sizereader;
        this.versionreader = versionreader;
    }

    @Override
    public FileTime lastModifiedTime() {
        return FileTime.from(versionreader.getAsLong(), TimeUnit.SECONDS);
//        return sysFileAttributeDelegate.lastModifiedTime();
    }

    @Override
    public FileTime lastAccessTime() {
        return sysFileAttributeDelegate.lastAccessTime();
    }

    @Override
    public FileTime creationTime() {
        return sysFileAttributeDelegate.creationTime();
    }

    @Override
    public boolean isRegularFile() {
        return sysFileAttributeDelegate.isRegularFile();
    }

    @Override
    public boolean isDirectory() {
        return sysFileAttributeDelegate.isDirectory();
    }

    @Override
    public boolean isSymbolicLink() {
        return sysFileAttributeDelegate.isSymbolicLink();
    }

    @Override
    public boolean isOther() {
        return sysFileAttributeDelegate.isOther();
    }

    @Override
    public long size() {
        return this.sizereader.getAsLong();
    }

    @Override
    public Object fileKey() {
        return null;
    }
}
