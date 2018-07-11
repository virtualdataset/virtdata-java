package io.virtdata.libimpl.continuous.impl.long_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.LogisticDistribution;

/**
 * {@inheritDoc}
 *
 * @see io.virtdata.libimpl.continuous.impl.long_double.LongToDoubleContinuousCurve
 */
@ThreadSafeMapper
public class Logistic extends LongToDoubleContinuousCurve {
    public Logistic(double mu, double scale, String... mods) {
        super(new LogisticDistribution(mu, scale), mods);
    }
}
