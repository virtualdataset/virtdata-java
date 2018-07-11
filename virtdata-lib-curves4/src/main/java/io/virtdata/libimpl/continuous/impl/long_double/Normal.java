package io.virtdata.libimpl.continuous.impl.long_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.NormalDistribution;

/**
 * {@inheritDoc}
 *
 * @see io.virtdata.libimpl.continuous.impl.long_double.LongToDoubleContinuousCurve
 */
@ThreadSafeMapper
public class Normal extends LongToDoubleContinuousCurve {
    public Normal(double mean, double sd, String... mods) {
        super(new NormalDistribution(mean, sd), mods);
    }
}
