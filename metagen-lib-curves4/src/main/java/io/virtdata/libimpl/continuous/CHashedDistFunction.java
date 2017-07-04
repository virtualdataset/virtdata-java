package io.virtdata.libimpl.continuous;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.libimpl.ThreadSafeHash;
import org.apache.commons.math4.distribution.RealDistribution;

import java.util.function.LongToDoubleFunction;

@ThreadSafeMapper
public class CHashedDistFunction implements LongToDoubleFunction {

    private final RealDistribution idist;
    private ThreadSafeHash hash = new ThreadSafeHash();
    private static double MAX_LONG_DOUBLE = (double) Long.MAX_VALUE;

    public CHashedDistFunction(RealDistribution idist) {
        this.idist = idist;
    }

    @Override
    public double applyAsDouble(long value) {
        long result = hash.applyAsLong(value);
        double unitSample = ((double) result)/MAX_LONG_DOUBLE;

        double sample = idist.inverseCumulativeProbability(unitSample);
        return sample;
    }
}
