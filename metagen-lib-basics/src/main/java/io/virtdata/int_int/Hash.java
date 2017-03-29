package io.virtdata.int_int;

import de.greenrobot.common.hash.Murmur3F;

import java.nio.ByteBuffer;
import java.util.function.IntUnaryOperator;

/**
 * This uses the Murmur3F (64-bit optimized) version of Murmur3,
 * not as a checksum, but as a simple hash. It doesn't bother
 * pushing the high-64 bits of input, since it only uses the lower
 * 64 bits of output. It does, however, return the absolute value.
 * This is to make it play nice with users and other libraries.
 */
public class Hash implements IntUnaryOperator {

    private ThreadLocal<ByteBuffer> tlbb = new ThreadLocal<ByteBuffer>() {
        @Override
        protected ByteBuffer initialValue() {
            return ByteBuffer.allocate(Long.BYTES);
        }
    };

    private Murmur3F murmur3F= new Murmur3F();

    @Override
    public int applyAsInt(int operand) {
        ByteBuffer bb = tlbb.get();
        murmur3F.reset();
        bb.putInt(0,operand);
        bb.putInt(4,operand);
        bb.position(0);
        murmur3F.update(bb.array());
        long result= Math.abs(murmur3F.getValue());
        return (int) result & Integer.MAX_VALUE;
    }
}
