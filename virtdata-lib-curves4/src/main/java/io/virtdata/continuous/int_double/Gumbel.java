package io.virtdata.continuous.int_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.GumbelDistribution;

/**
 * {@inheritDoc}
 */
@ThreadSafeMapper
public class Gumbel extends IntToDoubleContinuousCurve {
    public Gumbel(double mu, double beta, String... mods) {
        super(new GumbelDistribution(mu, beta), mods);
    }
}
