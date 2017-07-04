package io.virtdata.libimpl.discrete;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.libimpl.ThreadSafeHash;
import org.apache.commons.math4.distribution.IntegerDistribution;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class IHashedDistFunction implements LongUnaryOperator {

    private final IntegerDistribution idist;
    private static double MAX_LONG_DOUBLE = (double) Long.MAX_VALUE;
    private ThreadSafeHash hash = new ThreadSafeHash();

    public IHashedDistFunction(IntegerDistribution idist) {
        this.idist = idist;
    }

    @Override
    public long applyAsLong(long operand) {
        long result = hash.applyAsLong(operand);
        double unitSample = ((double) result)/MAX_LONG_DOUBLE;

        int sample = idist.inverseCumulativeProbability(unitSample);
        return sample;
    }
}
