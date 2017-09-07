package io.virtdata.libimpl.continuous;

import org.apache.commons.math4.distribution.RealDistribution;

import java.util.function.DoubleUnaryOperator;

public class RealDistributionICDSource implements DoubleUnaryOperator {

    private RealDistribution realDistribution;

    public RealDistributionICDSource(RealDistribution realDistribution) {
        this.realDistribution = realDistribution;
    }

    @Override
    public double applyAsDouble(double operand) {
        return realDistribution.inverseCumulativeProbability(operand);
    }
}
