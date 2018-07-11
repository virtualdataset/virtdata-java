package io.virtdata.libimpl.continuous.impl.long_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.LogNormalDistribution;

/**
 * {@inheritDoc}
 *
 * @see io.virtdata.libimpl.continuous.impl.long_double.LongToDoubleContinuousCurve
 */
@ThreadSafeMapper
public class LogNormal extends LongToDoubleContinuousCurve {
    public LogNormal(double scale, double shape, String... mods) {
        super(new LogNormalDistribution(scale, shape), mods);
    }
}
