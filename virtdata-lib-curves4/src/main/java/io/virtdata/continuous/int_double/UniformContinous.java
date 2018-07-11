package io.virtdata.continuous.int_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.UniformContinuousDistribution;

/**
 * {@inheritDoc}
 */
@ThreadSafeMapper
public class UniformContinous extends IntToDoubleContinuousCurve {
    public UniformContinous(double lower, double upper, String... mods) {
        super(new UniformContinuousDistribution(lower, upper), mods);
    }
}
