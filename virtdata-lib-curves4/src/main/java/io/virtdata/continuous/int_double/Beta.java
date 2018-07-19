package io.virtdata.continuous.int_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.BetaDistribution;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Beta_distribution">Wikipedia: Beta distribution</a>
 * @see <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/BetaDistribution.html">Commons JavaDoc: BetaDistribution</a>
 *
 * {@inheritDoc}
 */
@ThreadSafeMapper
public class Beta extends IntToDoubleContinuousCurve {
    public Beta(double alpha, double beta, String... mods) {
        super(new BetaDistribution(alpha, beta), mods);
    }
}
