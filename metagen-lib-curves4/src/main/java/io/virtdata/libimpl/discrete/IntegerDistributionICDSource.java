package io.virtdata.libimpl.discrete;

import org.apache.commons.math4.distribution.IntegerDistribution;

import java.util.function.DoubleToIntFunction;

public class IntegerDistributionICDSource implements DoubleToIntFunction {

    private IntegerDistribution integerDistribution;

    public IntegerDistributionICDSource(IntegerDistribution integerDistribution) {
        this.integerDistribution = integerDistribution;
    }

    @Override
    public int applyAsInt(double value) {
        return integerDistribution.inverseCumulativeProbability(value);
    }
}
