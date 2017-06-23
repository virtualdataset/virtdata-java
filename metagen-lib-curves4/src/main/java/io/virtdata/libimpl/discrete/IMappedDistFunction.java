package io.virtdata.libimpl.discrete;

import org.apache.commons.math4.distribution.IntegerDistribution;

import java.util.function.LongToIntFunction;

public class IMappedDistFunction implements LongToIntFunction {

    private final IntegerDistribution idist;
    private static double MAX_LONG_DOUBLE = (double) Long.MAX_VALUE;

    public IMappedDistFunction(IntegerDistribution idist) {
        this.idist = idist;
    }

    @Override
    public int applyAsInt(long value) {
        double unitSample = ((double) value)/MAX_LONG_DOUBLE;

        int sample = idist.inverseCumulativeProbability(unitSample);
        return sample;
    }
}
