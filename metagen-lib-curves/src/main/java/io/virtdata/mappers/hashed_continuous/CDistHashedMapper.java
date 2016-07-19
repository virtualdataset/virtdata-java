package io.virtdata.mappers.hashed_continuous;

import de.greenrobot.common.hash.Murmur3F;
import io.virtdata.mappers.mapped_continuous.CDistMappedResolver;

import java.nio.ByteBuffer;
import java.util.function.LongToDoubleFunction;

public class CDistHashedMapper implements LongToDoubleFunction {
    private CDistMappedResolver.ThreadSafe cdist;

    private Murmur3F murmur3F = new Murmur3F();
    ThreadLocal<ByteBuffer> tlbb = new ThreadLocal<ByteBuffer>() {
        @Override
        protected ByteBuffer initialValue() {
            return ByteBuffer.allocate(Long.BYTES);
        }
    };

    public CDistHashedMapper(String... args) {
        cdist = new CDistMappedResolver.ThreadSafe(args);
    }

    @Override
    public double applyAsDouble(long value) {
        ByteBuffer bb = tlbb.get();
        murmur3F.reset();
        bb.putLong(0,value);
        bb.position(0);
        murmur3F.update(bb.array());
        long result= Math.abs(murmur3F.getValue());

        double d = cdist.get().applyAsDouble(value);
        return d;
    }
}
