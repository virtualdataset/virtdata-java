package io.virtdata.libimpl.discrete;

import com.google.auto.service.AutoService;
import io.virtdata.api.DataMapperLibrary;
import io.virtdata.api.ValueType;
import io.virtdata.api.specs.SpecData;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.reflection.ConstructorResolver;
import io.virtdata.reflection.DeferredConstructor;
import org.apache.commons.math4.distribution.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.*;

/**
 * <p>This mapper provides inverse cumulative distribution sampling for integer-valued
 * distributions. This works by mapping a long value between 0L and Long.MAX_VALUE
 * to a double value in the unit interval [0.0,1.0], and then applying this to
 * an inverse cumulative distribution function that is distribution specific.</p>
 * <p>Some distributions are realistic in their shape, but not
 * practical in their computational overhead. For these, you can use an interpolated
 * form, in which the function is sampled equidistantly over the unit interval at
 * some resolution, and then all subsequent lookups are computed from simple
 * linear interpolation. This offers the trade-off of a very slight degradation of
 * accuracy (arguably dependent on the actual density curve in question), with the ability
 * to make all statistical sampling perform in O(1) time, and possibly more importantly,
 * the same time as all other statistical curves.</p>
 * <p>There are various ways to configure these distributions. For each, there are
 * two choices to be made: mapping vs hashing, and computing vs interpolating.</p>
 * <p>Mapping vs Hashing</p>
 * <UL>
 * <LI><strong>mapto_</strong> - When a specifier
 * contains <em>mapto_</em>, direct ICDF mapping
 * mode is used. This allows for selecting a value over
 * the distribution by providing the unit sample value directly. Instead of
 * "randomizing" the sample value selector, it simply accesses the ICDF value directly.
 * In this way, it is mapping the unit interval to the ICDF curve.</LI>
 * <LI><strong>hashto_</strong> (default) - When a specifier contains <em>hashto_</em>,
 * then a "sampling" mode is used. This takes the input long value and applies a
 * consistent hash to it, then scales it to a double value in the unit interval
 * before drawing a sample from the curve.</LI>
 * </UL>
 * <p>Computing vs Interpolating</p>
 * <UL>
 * <LI><strong>interpolate_</strong> (default) - When a specifier contains <em>interpolate_</em>, then the
 * inverse cumulative density curve
 * is pre-sampled at some resolution (1000 by default) over the unit interval.
 * Any values read from the ICDF are then read instead from the interpolation table.
 * This is done by default unless you specify compute_ as explained below.</LI>
 * <LI><strong>compute_</strong> - When a specifier contains <em>compute_</em>, then every
 * time you ask for a value from the function, the inverse cumulative density function is
 * calculated for a given point on the unit interval. This can be expensive, as well as
 * introduce variance in your generation times depending on the distribution.
 * </UL>
 * <p>It is valid to include mapto_ or hashto_ and interpolate_ or
 * compute_ in your specifiers any order, so long as there are no conflicts.
 * </p>
 * <p>Examples:</p>
 * <ul>
 * <li>zipf(10,5.0) - use interpolation and hashing to simulate sampling from
 * the zipf distribution, with 10 elements and exponent of 5.0. Because no
 * qualifiers were included as explained above, hashto_ and interpolate_
 * were implied.</li>
 * <li>mapto_zipf(10,5.0) - Same distribution as above, but use ICDF mapping
 * instead of hashing. Continue to use interpolation as implied</li>
 * <li>compute_zipf(10,5.0) - Same distribution as above, but enforce
 * non-interpolated mode. Since mapto_ and hashto_ weren't provided,
 * hashto_ is implied.</li>
 * <li>mapto_compute_zipf(10,5.0) - This will be very slow. This forces
 * computation of a harmonic series over N elements for <em>every single
 * access.</em> Interpolation is usually the better way.</li>
 * </ul>
 */
@AutoService(DataMapperLibrary.class)
public class IntegerDistributions implements DataMapperLibrary {

    private static final String MAPTO = "mapto_";
    private static final String HASHTO = "hashto_";
    private static final String COMPUTE = "compute_";
    private static final String INTERPOLATE = "interpolate_";

    public static LongUnaryOperator forSpec(String spec) {
        Optional<ResolvedFunction> resolvedFunction = new IntegerDistributions().resolveFunction(spec);
        return resolvedFunction
                .map(ResolvedFunction::getFunctionObject)
                .map(f -> ((LongUnaryOperator) f))
                .orElseThrow(() -> new RuntimeException("Invalid spec: " + spec));
    }

    private static String distributionNameFor(String specName) {
        return specName.replaceAll(COMPUTE, "")
                .replaceAll(INTERPOLATE, "")
                .replaceAll(MAPTO, "")
                .replaceAll(HASHTO, "");
    }

    @Override
    public String getLibraryName() {
        return "math4-dcurves";
    }

    @Override
    public boolean canParseSpec(String specifier) {
        Optional<SpecData> optionalData = SpecData.forOptionalSpec(specifier);
        if (!optionalData.isPresent()) {
            return false;
        }
        SpecData specData = optionalData.get();
        try {
            String distName = distributionNameFor(specData.getFuncName());
            IntegerDistribution.valueOf(distName);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        return resolveFunction(spec,ValueType.LONG, ValueType.LONG);
    }

    public Optional<ResolvedFunction> resolveFunction(String spec, ValueType inputType, ValueType outputType) {
        if (!canParseSpec(spec)) {
            return Optional.empty();
        }
        SpecData specData = SpecData.forSpec(spec);
        String funcName = specData.getFuncName();
        IntegerDistribution integerDistribution =
                IntegerDistribution.valueOf(distributionNameFor(funcName));
        Class<? extends AbstractIntegerDistribution> distributionClass = integerDistribution.getDistributionClass();
        DeferredConstructor<? extends AbstractIntegerDistribution> deferred =
                ConstructorResolver.resolve(distributionClass, specData.getArgs());
        AbstractIntegerDistribution distribution = deferred.construct();

        boolean interpolate = !funcName.contains(COMPUTE) || funcName.contains(INTERPOLATE);
        boolean hashto = !funcName.contains(MAPTO) || funcName.contains(HASHTO);

        DoubleToIntFunction icdSource = new IntegerDistributionICDSource(distribution);

        if (inputType== ValueType.LONG && outputType==ValueType.LONG) {
            LongUnaryOperator samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingLongLongSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new DiscreteLongLongSampler(icdSource, hashto);
            }
            return Optional.of(new ResolvedFunction(samplingFunction, true));
        } else if (inputType==ValueType.INT && outputType==ValueType.LONG) {
            IntToLongFunction samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingIntLongSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new DiscreteIntLongSampler(icdSource, hashto);
            }
            return Optional.of(new ResolvedFunction(samplingFunction, true));
        } else if (inputType== ValueType.LONG && outputType==ValueType.INT) {
            LongToIntFunction samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingLongIntSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new DiscreteLongIntSampler(icdSource, hashto);
            }
            return Optional.of(new ResolvedFunction(samplingFunction, true));
        } else if (inputType==ValueType.INT && outputType==ValueType.INT) {
            IntUnaryOperator samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingIntIntSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new DiscreteIntIntSampler(icdSource, hashto);
            }
            return Optional.of(new ResolvedFunction(samplingFunction, true));
        }
        return Optional.empty();
    }

    @Override
    public List<ResolvedFunction> resolveFunctions(String specifier) {
        List<ResolvedFunction> resolvedList = new ArrayList<>();
        resolveFunction(specifier,ValueType.LONG, ValueType.LONG).map(resolvedList::add);
        resolveFunction(specifier,ValueType.LONG, ValueType.INT).map(resolvedList::add);
        resolveFunction(specifier,ValueType.INT, ValueType.LONG).map(resolvedList::add);
        resolveFunction(specifier,ValueType.INT, ValueType.INT).map(resolvedList::add);

        Optional<ResolvedFunction> resolvedFunction = resolveFunction(specifier);
        resolvedFunction.map(resolvedList::add);
        return resolvedList;
    }

    @Override
    public List<String> getDataMapperNames() {
        List<String> names = new ArrayList<>();
        Arrays.stream(IntegerDistribution.values()).map(String::valueOf).forEach(
                n -> {
                    names.add(n);
                    names.add("mapto_" + n);
                    names.add("mapto_compute_" + n);
                    names.add("compute_" + n);
                }
        );
        return names;
    }

    private enum IntegerDistribution {
        hypergeometric(HypergeometricDistribution.class), // hypergeometric(40,20,10) (int pop, int successes, int samples)
        uniform_integer(UniformIntegerDistribution.class), // uniform(0,100) (int min, int max)
        geometric(GeometricDistribution.class), // geometric(0.5) (double probability)
        poisson(PoissonDistribution.class), // poisson(5.0) (double avgrate)
        zipf(ZipfDistribution.class), // zipf(10000, 5.0) (int elements, double exponent)
        binomial(BinomialDistribution.class), // binomial(8,0.5) - (int trials, double probability)
        pascal(PascalDistribution.class); // pascal(10,0.33) - (int successes, double probability)

        private final Class<? extends AbstractIntegerDistribution> distribution;

        IntegerDistribution(Class<? extends AbstractIntegerDistribution> distribution) {
            this.distribution = distribution;
        }

        public Class<? extends AbstractIntegerDistribution> getDistributionClass() {
            return distribution;
        }
    }


}
