package io.virtdata.from_long.to_int;

import de.greenrobot.common.hash.Murmur3F;
import io.virtdata.api.ThreadSafeMapper;

import java.nio.ByteBuffer;
import java.util.function.LongToIntFunction;

/**
 * This uses the Murmur3F (64-bit optimized) version of Murmur3,
 * not as a checksum, but as a simple hash. It doesn't bother
 * pushing the high-64 bits of input, since it only uses the lower
 * 64 bits of output. This version returns the full signed result.
 */
@ThreadSafeMapper
public class SignedHash implements LongToIntFunction {

    ThreadLocal<ByteBuffer> bb_TL = ThreadLocal.withInitial(() -> ByteBuffer.allocate(Long.BYTES));
    ThreadLocal<Murmur3F> murmur3f_TL = ThreadLocal.withInitial(Murmur3F::new);

    @Override
    public int applyAsInt(long value) {
        Murmur3F murmur3F = murmur3f_TL.get();
        ByteBuffer bb = bb_TL.get();
        murmur3F.reset();
        bb.putLong(0,value);
        murmur3F.update(bb.array(),0,Long.BYTES);
        long result= murmur3F.getValue();
        return (int) (result & Integer.MAX_VALUE);
    }
}
