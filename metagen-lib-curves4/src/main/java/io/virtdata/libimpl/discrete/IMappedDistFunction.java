package io.virtdata.libimpl.discrete;

import io.virtdata.api.ThreadSafeMapper;
import org.apache.commons.math4.distribution.IntegerDistribution;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class IMappedDistFunction implements LongUnaryOperator {

    private final IntegerDistribution idist;
    private static double MAX_LONG_DOUBLE = (double) Long.MAX_VALUE;

    public IMappedDistFunction(IntegerDistribution idist) {
        this.idist = idist;
    }

    @Override
    public long applyAsLong(long operand) {
        double unitSample = ((double) operand)/MAX_LONG_DOUBLE;

        int sample = idist.inverseCumulativeProbability(unitSample);
        return sample;
    }
}
