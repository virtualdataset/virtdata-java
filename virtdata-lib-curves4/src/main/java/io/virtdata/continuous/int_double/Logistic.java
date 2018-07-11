package io.virtdata.continuous.int_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.LogisticDistribution;

/**
 * {@inheritDoc}
 */
@ThreadSafeMapper
public class Logistic extends IntToDoubleContinuousCurve {
    public Logistic(double mu, double scale, String... mods) {
        super(new LogisticDistribution(mu, scale), mods);
    }
}
