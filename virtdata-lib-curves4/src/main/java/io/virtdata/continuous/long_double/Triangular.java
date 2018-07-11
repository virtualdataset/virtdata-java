package io.virtdata.continuous.long_double;

import io.virtdata.annotations.ThreadSafeMapper;
import org.apache.commons.statistics.distribution.TriangularDistribution;

/**
 * {@inheritDoc}
 *
 * @see io.virtdata.continuous.long_double.LongToDoubleContinuousCurve
 */
@ThreadSafeMapper
public class Triangular extends LongToDoubleContinuousCurve {
    public Triangular(double a, double c, double b, String... mods) {
        super(new TriangularDistribution(a,c,b), mods);
    }
}
