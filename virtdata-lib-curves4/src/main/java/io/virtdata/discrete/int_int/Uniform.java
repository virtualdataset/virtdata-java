package io.virtdata.discrete.int_int;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.UniformDiscreteDistribution;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Discrete_uniform_distribution">Wikipedia: Discrete uniform distribution</a>
 * @see <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/UniformDiscreteDistribution.html">Commons JavaDoc: UniformDiscreteDistribution</a>
 *
 * {@inheritDoc}
 */
@ThreadSafeMapper
public class Uniform extends IntToIntDiscreteCurve {
    public Uniform(int lower, int upper, String... modslist) {
        super(new UniformDiscreteDistribution(lower, upper), modslist);
    }
}
