package io.virtdata.libimpl.continuous.impl.long_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.ChiSquaredDistribution;

/**
 * {@inheritDoc}
 *
 * @see io.virtdata.libimpl.continuous.impl.long_double.LongToDoubleContinuousCurve
 */
@ThreadSafeMapper
public class ChiSquared extends LongToDoubleContinuousCurve {
    public ChiSquared(double degreesOfFreedom, String... mods) {
        super(new ChiSquaredDistribution(degreesOfFreedom), mods);
    }
}
