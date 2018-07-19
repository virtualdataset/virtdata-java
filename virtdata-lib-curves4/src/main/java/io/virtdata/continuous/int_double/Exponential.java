package io.virtdata.continuous.int_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.ExponentialDistribution;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Exponential_distribution">Wikipedia: Exponential distribution</a>
 * @see <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/ExponentialDistribution.html">Commons JavaDoc: ExponentialDistribution</a>
 *
 * {@inheritDoc}
 */
@ThreadSafeMapper
public class Exponential extends IntToDoubleContinuousCurve {
    public Exponential(double mean, String... mods) {
        super(new ExponentialDistribution(mean), mods);
    }
}
