package io.virtdata.gen.internal;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.function.LongFunction;

/**
 * Generates a long value representing a sampled value from a cumulative
 * density function for a binomial curve.
 *
 * <p>This class does nothing to handle randomness or the need to choose
 * over the possible interval pseudo-randomly. It simply provides the
 * transform between the "U" unit-interval value that you would get
 * from that phase and the resulting value on the cumulative density curve.</p>
 *
 * <p>Furthermore, the input value is not in actual "U" unit-interval form.
 * It is instead a value from the set of positive long values.</p>
 *
 */
public class LogNormalAdapter implements LongFunction<Double> {

    private final RandomBypassAdapter randomBypassAdapter;
    private final RealDistribution realDistribution;

    public LogNormalAdapter(double mean, double stddev) {
        this.randomBypassAdapter = new RandomBypassAdapter();
        this.realDistribution = new LogNormalDistribution(randomBypassAdapter,mean,stddev);
    }

    public RealDistribution getDistribution() {
        return realDistribution;
    }

    @Override
    public Double apply(long operand) {
        randomBypassAdapter.setSeed(operand);
        return realDistribution.sample();
    }
}
