package io.virtdata.docsys.metafs.fs.renderfs.fs;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;

public class RenderedFileChannel extends FileChannel {

    private final SeekableByteChannel bytes;

    public RenderedFileChannel(SeekableByteChannel bytes) {
        this.bytes = bytes;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return bytes.read(dst);
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        throw new RuntimeException("implement me");
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        throw new UnsupportedOperationException("You can't write to this read-only channel");
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        throw new UnsupportedOperationException("You can't write to this read-only channel");
    }

    @Override
    public long position() throws IOException {
        return bytes.position();
    }

    @Override
    public FileChannel position(long newPosition) throws IOException {
        bytes.position(newPosition);
        return this;
    }

    @Override
    public long size() throws IOException {
        return bytes.size();
    }

    @Override
    public FileChannel truncate(long size) throws IOException {
        throw new UnsupportedOperationException("You can't write to this read-only channel");
    }

    @Override
    public void force(boolean metaData) throws IOException {
        throw new UnsupportedOperationException("You can't write to this read-only channel");
    }

    @Override
    public long transferTo(long position, long count, WritableByteChannel target) throws IOException {
        bytes.position(position);
        ByteBuffer txfer = ByteBuffer.wrap(new byte[(int) count]);
        int readBytes = bytes.read(txfer);
        txfer.flip();
        target.write(txfer);
        return readBytes;
    }

    @Override
    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        throw new UnsupportedOperationException("You can't write to this read-only channel");
    }

    @Override
    public int read(ByteBuffer dst, long position) throws IOException {
        bytes.position(position);
        return bytes.read(dst);
    }

    @Override
    public int write(ByteBuffer src, long position) throws IOException {
        throw new UnsupportedOperationException("You can't write to this read-only channel");
    }

    @Override
    public MappedByteBuffer map(MapMode mode, long position, long size) throws IOException {
        ByteBuffer newBuf = MappedByteBuffer.allocate((int) size);
        if (newBuf instanceof MappedByteBuffer) {
            MappedByteBuffer mbb = (MappedByteBuffer) newBuf;
            bytes.position(position);
            bytes.read(mbb);
            return mbb;
        } else {
            throw new RuntimeException("unable to ensure mapped byte buffer.");
        }
    }

    @Override
    public FileLock lock(long position, long size, boolean shared) throws IOException {
        return null;
    }

    @Override
    public FileLock tryLock(long position, long size, boolean shared) throws IOException {
        return null;
    }

    @Override
    protected void implCloseChannel() throws IOException {
    }
}
