package io.virtdata.discrete.int_int;

import io.virtdata.discrete.common.DiscreteIntIntSampler;
import io.virtdata.discrete.common.IntegerDistributionICDSource;
import io.virtdata.discrete.common.InterpolatingIntIntSampler;
import org.apache.commons.statistics.distribution.DiscreteDistribution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.DoubleToIntFunction;
import java.util.function.IntUnaryOperator;

/**
 * Generate samples according to the specified probability density.
 *
 * The input value consists of a long between 0L and Long.MAX_VALUE.
 * This value is scaled to the unit interval (0.0, 1.0) as
 * an index into a sampling function based on inverse cumulative
 * density sampling.
 *
 * <H3>Sampling Mode</H3>
 *
 * The curve can be sampled in either map or hash mode. Map mode
 * simply indexes into the probability curve in the order that
 * it would appear on a density plot. Hash mode applies a
 * murmur3 hash to the input value before scaling from the
 * range of longs to the unit interval, thus providing a pseudo-random
 * sample of a value from the curve. This is usually what you want,
 * so hash mode is the default.  To enable map mode, simply provide
 * "map" as one of the modifiers as explained below.
 *
 * <H3>Interpolation</H3>
 *
 * The curve can be computed from the sampling function for each value
 * generated, or it can be provided via interpolation with a lookup table.
 * Using interpolation makes all the generator functions perform the
 * same. This is almost always what you want, so interpolation is
 * enabled by default. In order to compute the value for every sample
 * instead, simply provide "compute" as one of the modifiers as explained
 * below.
 *
 * You can add optional modifiers after the distribution parameters.
 * You can add one of 'hash' or 'map' but not both. If neither of these is
 * added, 'hash' is implied as a default.
 * You can add one of 'interpolate' or 'compute' but not both. If neither
 * of these is added, 'interpolate' is implied as a default.
 *
 * At times, it might be useful to add 'hash', 'interpolate' to your
 * specifiers as a form of verbosity or explicit specification.
 */

public class IntToIntDiscreteCurve implements IntUnaryOperator {

    private DiscreteDistribution distribution;
    private IntUnaryOperator function;

    private final static HashSet<String> validModifiers = new HashSet<String>() {{
        add("compute");
        add("interpolate");
        add("map");
        add("hash");
    }};


    public IntToIntDiscreteCurve(DiscreteDistribution distribution, String... modslist) {
        this.distribution = distribution;
        HashSet<String> mods = new HashSet<>(Arrays.asList(modslist));

        DoubleToIntFunction icdSource = new IntegerDistributionICDSource(distribution);

        if (mods.contains("hash") && mods.contains("map")) {
            throw new RuntimeException("mods must not contain both hash and map.");
        }
        if (mods.contains("interpolate") && mods.contains("compute")) {
            throw new RuntimeException("mods must not contain both interpolate and compute");
        }
        for (String s : modslist) {
            if (!validModifiers.contains(s)) {
                throw new RuntimeException("modifier '" + s + "' is not a valid modifier. Use one of " + validModifiers.toString() + " instead.");
            }
        }

        boolean hash = ( mods.contains("hash") || !mods.contains("map"));
        boolean interpolate = ( mods.contains("interpolate") || !mods.contains("compute"));

        function = interpolate ?
                new InterpolatingIntIntSampler(icdSource, 1000, hash)
                :
                new DiscreteIntIntSampler(icdSource, hash);
    }

    @Override
    public int applyAsInt(int operand) {
        return function.applyAsInt(operand);
    }
}
