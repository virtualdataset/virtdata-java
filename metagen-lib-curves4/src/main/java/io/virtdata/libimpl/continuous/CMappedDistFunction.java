package io.virtdata.libimpl.continuous;

import org.apache.commons.math4.distribution.RealDistribution;

import java.util.function.LongToDoubleFunction;

public class CMappedDistFunction implements LongToDoubleFunction {

    private final RealDistribution idist;
    private static double MAX_LONG_DOUBLE = (double) Long.MAX_VALUE;

    public CMappedDistFunction(RealDistribution idist) {
        this.idist = idist;
    }

    @Override
    public double applyAsDouble(long value) {
        double unitSample = ((double) value)/MAX_LONG_DOUBLE;
        double sample = idist.inverseCumulativeProbability(unitSample);
        return sample;
    }
}
