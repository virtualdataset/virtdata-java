package io.virtdata.libimpl.continuous.impl.long_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.ConstantContinuousDistribution;

/**
 *
 * Always outputs the same value
 *
 * {@inheritDoc}
 *
 * @see io.virtdata.libimpl.continuous.impl.long_double.LongToDoubleContinuousCurve
 */
@ThreadSafeMapper
public class ConstantContinuous extends LongToDoubleContinuousCurve {
    public ConstantContinuous(double value, String... mods) {
        super(new ConstantContinuousDistribution(value), mods);
    }
}
