package io.virtdata.libimpl.continuous;

import de.greenrobot.common.hash.Murmur3F;
import org.apache.commons.math4.distribution.RealDistribution;

import java.nio.ByteBuffer;
import java.util.function.LongToDoubleFunction;

public class CHashedDistFunction implements LongToDoubleFunction {

    private final RealDistribution idist;
    private final Murmur3F murmur3F = new Murmur3F();
    private final ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
    private static double MAX_LONG_DOUBLE = (double) Long.MAX_VALUE;

    public CHashedDistFunction(RealDistribution idist) {
        this.idist = idist;
    }

    @Override
    public double applyAsDouble(long value) {
        murmur3F.reset();
        bb.putLong(0,value);
        bb.position(0);
        murmur3F.update(bb.array());
        long result= Math.abs(murmur3F.getValue());
        double unitSample = ((double) result)/MAX_LONG_DOUBLE;

        double sample = idist.inverseCumulativeProbability(unitSample);
        return sample;
    }
}
