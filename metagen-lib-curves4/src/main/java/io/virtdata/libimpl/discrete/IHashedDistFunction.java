package io.virtdata.libimpl.discrete;

import de.greenrobot.common.hash.Murmur3F;
import org.apache.commons.math4.distribution.IntegerDistribution;

import java.nio.ByteBuffer;
import java.util.function.LongToIntFunction;

public class IHashedDistFunction implements LongToIntFunction {

    private final IntegerDistribution idist;
    private final Murmur3F murmur3F = new Murmur3F();
    private final ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
    private static double MAX_LONG_DOUBLE = (double) Long.MAX_VALUE;

    public IHashedDistFunction(IntegerDistribution idist) {
        this.idist = idist;
    }

    @Override
    public int applyAsInt(long value) {
        murmur3F.reset();
        bb.putLong(0,value);
        bb.position(0);
        murmur3F.update(bb.array());
        long result= Math.abs(murmur3F.getValue());
        double unitSample = ((double) result)/MAX_LONG_DOUBLE;

        int sample = idist.inverseCumulativeProbability(unitSample);
        return sample;
    }
}
