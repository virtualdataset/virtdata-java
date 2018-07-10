package io.virtdata.libimpl.continuous.impl;

import io.virtdata.libimpl.continuous.InterpolatingLongDoubleSampler;
import io.virtdata.libimpl.continuous.RealDistributionICDSource;
import io.virtdata.libimpl.continuous.RealLongDoubleSampler;
import org.apache.commons.statistics.distribution.ContinuousDistribution;
import org.apache.commons.statistics.distribution.LevyDistribution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.DoubleUnaryOperator;
import java.util.function.LongToDoubleFunction;

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
