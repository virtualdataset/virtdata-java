package io.virtdata.mappers.mapped_continuous;

import io.virtdata.libimpl.RandomBypassAdapter;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.function.LongToDoubleFunction;

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
public class CDistMappedCoupler<T extends RealDistribution> implements LongToDoubleFunction {

    private final RandomBypassAdapter randomBypassAdapter;
    private final RealDistribution distribution;

    private Class<T> distributionClass;
    private Object[] args;

    public CDistMappedCoupler(RealDistribution distribution, RandomBypassAdapter bypass) {
        this.randomBypassAdapter = bypass;
        this.distribution = distribution;
    }

    public RealDistribution getDistribution() {
        return distribution;
    }

    @Override
    public double applyAsDouble(long value) {
        randomBypassAdapter.setSeed(value);
        double sample = distribution.sample();
        return sample;
    }
}
