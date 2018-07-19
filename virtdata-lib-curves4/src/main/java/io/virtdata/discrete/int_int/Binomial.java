package io.virtdata.discrete.int_int;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.BinomialDistribution;

/**
 * @see <a href="http://en.wikipedia.org/wiki/Binomial_distribution">Wikipedia: Binomial distribution</a>
 * @see <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/BinomialDistribution.html">Commons JavaDoc: BinomialDistribution</a>
 *
 * {@inheritDoc}
 */
@ThreadSafeMapper
public class Binomial extends IntToIntDiscreteCurve {
    public Binomial(int trials, double p, String... modslist) {
        super(new BinomialDistribution(trials, p), modslist);
    }
}
