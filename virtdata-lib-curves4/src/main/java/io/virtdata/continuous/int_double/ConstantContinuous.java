package io.virtdata.continuous.int_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.ConstantContinuousDistribution;

/**
 * Always yields the same value
 * @see <a href="https://commons.apache.org/proper/commons-statistics/commons-statistics-distribution/apidocs/org/apache/commons/statistics/distribution/ConstantContinuousDistribution.html">Commons JavaDoc: ConstantContinuousDistribution</a>
 *
 * {@inheritDoc}
 */
@ThreadSafeMapper
public class ConstantContinuous extends IntToDoubleContinuousCurve {
    public ConstantContinuous(double value, String... mods) {
        super(new ConstantContinuousDistribution(value), mods);
    }
}
