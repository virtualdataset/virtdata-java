package io.virtdata.libimpl.continuous.impl.long_double;

import io.virtdata.libimpl.continuous.InterpolatingLongDoubleSampler;
import io.virtdata.libimpl.continuous.RealDistributionICDSource;
import io.virtdata.libimpl.continuous.RealLongDoubleSampler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.DoubleUnaryOperator;
import java.util.function.LongToDoubleFunction;
import org.apache.commons.statistics.distribution.ContinuousDistribution;

/**
 * Additionally you can provide mods as comma separated strings
 *
 * Randomization method:
 * hash - murmur3 to get random values as inputs to the generator
 * map - samples the inverse cumulative density in order accross unit interval (maps the input (cycle) over the probability curve in order)
 *
 * Computation method:
 * interpolate - (default) generates 1000 points using the Apache Commons library and interpolates from there. Guaranteed uniform performance across generator functions.
 * compute - does not interpolate, uses the commons lib every time
 *
 * i.e. - CurveFunction(1.1, 1.2, hash, compute)
 * i.e. - CurveFunction(1.1, 1.2, hash, interpolate)
 * i.e. - CurveFunction(1.1, 1.2, map, compute)
 * i.e. - CurveFunction(1.1, 1.2, map, interpolate)
 *
 * Note: the input for the function (cycle) is a long but is converted into a double between 0.0 to 1.0 for statistical reasons.
 */

public class LongToDoubleContinuousCurve implements LongToDoubleFunction {

    private ContinuousDistribution distribution;
    private LongToDoubleFunction function;

    public LongToDoubleContinuousCurve(ContinuousDistribution distribution, String... modslist) {
        this.distribution = distribution;
        HashSet<String> mods = new HashSet<>(Arrays.asList(modslist));

        DoubleUnaryOperator icdSource = new RealDistributionICDSource(distribution);

        if (mods.contains("hash") && mods.contains("map")) {
            throw new RuntimeException("mods must not contain both hash and map.");
        }
        if (mods.contains("interpolate") && mods.contains("compute")) {
            throw new RuntimeException("mods must not contain both interpolate and compute");
        }

        boolean hash = ( mods.contains("hash") || !mods.contains("map"));
        boolean interpolate = ( mods.contains("interpolate") || !mods.contains("compute"));

        function = interpolate ?
                new InterpolatingLongDoubleSampler(icdSource, 1000, hash)
                :
                new RealLongDoubleSampler(icdSource, hash);
    }

    @Override
    public double applyAsDouble(long value) {
        return function.applyAsDouble(value);
    }
}
