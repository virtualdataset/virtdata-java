package io.virtdata.mappers.discrete;

import io.virtdata.mappers.internal.RandomBypassAdapter;
import org.apache.commons.math3.distribution.IntegerDistribution;

import java.util.function.LongUnaryOperator;

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
public class IDistInverter<T extends IntegerDistribution> implements LongUnaryOperator {

    private final RandomBypassAdapter randomBypassAdapter;
    private final IntegerDistribution distribution;

    private Class<T> distributionClass;
    private Object[] args;

    public IDistInverter(IntegerDistribution integerDistribution) {
        this.randomBypassAdapter = new RandomBypassAdapter();
        this.distribution = integerDistribution;
    }

    public IntegerDistribution getDistribution() {
        return distribution;
    }

    @Override
    public long applyAsLong(long operand) {
        randomBypassAdapter.setSeed(operand);
        return (long) distribution.sample();
    }

}
