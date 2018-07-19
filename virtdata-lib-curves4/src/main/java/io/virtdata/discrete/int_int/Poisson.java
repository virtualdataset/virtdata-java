package io.virtdata.discrete.int_int;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.PoissonDistribution;

/**
 * @see <a href="http://en.wikipedia.org/wiki/Poisson_distribution">Wikipedia: Poisson distribution</a>
 * @see <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/PoissonDistribution.html">Commons JavaDoc: PoissonDistribution</a>
 *
 * {@inheritDoc}
 */
@ThreadSafeMapper
public class Poisson extends IntToIntDiscreteCurve {
    public Poisson(double p, String... modslist) {
        super(new PoissonDistribution(p), modslist);
    }
}
