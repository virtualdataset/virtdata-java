package io.virtdata.libimpl.continuous.impl.long_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.TDistribution;

/**
 * {@inheritDoc}
 *
 * @see io.virtdata.libimpl.continuous.impl.long_double.LongToDoubleContinuousCurve
 */
@ThreadSafeMapper
public class T extends LongToDoubleContinuousCurve {
    public T(double degreesOfFreedom, String... mods) {
        super(new TDistribution(degreesOfFreedom), mods);
    }
}
