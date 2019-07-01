package io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class VirtualFileBasicFileAttributes implements BasicFileAttributes {

    private BasicFileAttributes sysFileAttributeDelegate;
    private long virtualSize;

    public VirtualFileBasicFileAttributes(BasicFileAttributes sysFileAttributeDelegate, long virtualSize) {
        this.sysFileAttributeDelegate = sysFileAttributeDelegate;
        this.virtualSize = virtualSize;
    }

    @Override
    public FileTime lastModifiedTime() {
        return sysFileAttributeDelegate.lastModifiedTime();
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
        return this.virtualSize;
    }

    @Override
    public Object fileKey() {
        return null;
    }
}
