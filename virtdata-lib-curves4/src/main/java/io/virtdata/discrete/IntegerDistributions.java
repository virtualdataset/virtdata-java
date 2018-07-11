package io.virtdata.discrete;

import io.virtdata.api.ValueType;
import io.virtdata.api.VirtDataFunctionLibrary;
import io.virtdata.ast.FunctionCall;
import io.virtdata.ast.VirtDataFlow;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.parser.VirtDataDSL;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.statistics.distribution.*;

import java.lang.reflect.Constructor;
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
public class IntegerDistributions implements VirtDataFunctionLibrary {

    private static final String MAPTO = "mapto_";
    private static final String HASHTO = "hashto_";
    private static final String COMPUTE = "compute_";
    private static final String INTERPOLATE = "interpolate_";

    public static LongUnaryOperator forSpec(String spec) {
        VirtDataDSL.ParseResult parseResult = VirtDataDSL.parse(spec);
        if (parseResult.throwable!=null) {
            throw new RuntimeException(parseResult.throwable);
        }

        VirtDataFlow flow = parseResult.flow;
        if (flow.getExpressions().size()>1) {
            throw new RuntimeException("Unable to parse flows in " + IntegerDistributions.class);
        }
        FunctionCall call = flow.getLastExpression().getCall();
        Class<?> inType = Optional.ofNullable(call.getInputType()).map(ValueType::valueOfClassName).map(ValueType::getValueClass).orElse(null);
        Class<?> outType = Optional.ofNullable(call.getOutputType()).map(ValueType::valueOfClassName).map(ValueType::getValueClass).orElse(null);
        inType = (inType==null ? long.class : inType);
        outType = (outType==null ? long.class : outType);

        List<ResolvedFunction> resolvedFunctions = new IntegerDistributions().resolveFunctions(
                outType, inType, call.getFunctionName(), call.getArguments()
        );

        if (resolvedFunctions.size()>1) {
            throw new RuntimeException("Found " + resolvedFunctions.size() + " implementations, be more specific with" +
                    "input or output qualifiers as in int -> or -> long");
        }
        return (LongUnaryOperator) resolvedFunctions.get(0).getFunctionObject();
    }

    private static String distributionNameFor(String specName) {
        return specName.replaceAll(COMPUTE, "")
                .replaceAll(INTERPOLATE, "")
                .replaceAll(MAPTO, "")
                .replaceAll(HASHTO, "");
    }

    @Override
    public List<ResolvedFunction> resolveFunctions(Class<?> returnClass, Class<?> inputClass, String funcName, Object... parameters) {
        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();

        ValueType inputValueType = inputClass!=null ? ValueType.valueOfAssignableClass(inputClass) : null;
        ValueType outputValueType = returnClass!=null ? ValueType.valueOfAssignableClass(returnClass) : null;
//        inputValueType = inputValueType!=null ? inputValueType : ValueType.LONG;
//        outputValueType = outputValueType!=null ? outputValueType : ValueType.LONG;

        Optional<IntegerDistribution> optionalIntegerDistribution =
                IntegerDistribution.optionalValueOf(distributionNameFor(funcName));

        if (!optionalIntegerDistribution.isPresent()) {
            return resolvedFunctions;
        }

        Class<? extends DiscreteDistribution> distributionClass = optionalIntegerDistribution.get().getDistributionClass();

        Class<?>[] initTypes = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            initTypes[i] = parameters[i].getClass();
        }
        Constructor<? extends DiscreteDistribution> usingCtor =
                ConstructorUtils.getMatchingAccessibleConstructor(distributionClass, initTypes);

        DiscreteDistribution distribution;
        try {
            distribution = ConstructorUtils.invokeConstructor(distributionClass, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean interpolate = !funcName.contains(COMPUTE) || funcName.contains(INTERPOLATE);
        boolean hashto = !funcName.contains(MAPTO) || funcName.contains(HASHTO);

        DoubleToIntFunction icdSource = new IntegerDistributionICDSource(distribution);

        if ((inputValueType==null || inputValueType== ValueType.LONG)
                && (outputValueType==null || outputValueType==ValueType.LONG)) {
            LongUnaryOperator samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingLongLongSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new DiscreteLongLongSampler(icdSource, hashto);
            }
            resolvedFunctions.add(
                    new ResolvedFunction(
                            samplingFunction,
                            true,
                            usingCtor.getParameterTypes(), parameters,
                            Long.TYPE, Long.TYPE,
                            getName()
                    )
            );
        } else if ((inputValueType==null || inputValueType==ValueType.INT)
                && (outputValueType==null || outputValueType==ValueType.LONG)) {
            IntToLongFunction samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingIntLongSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new DiscreteIntLongSampler(icdSource, hashto);
            }
            resolvedFunctions.add(
                    new ResolvedFunction(
                            samplingFunction,
                            true,
                            usingCtor.getParameterTypes(), parameters,
                            Integer.TYPE, Long.TYPE,
                            getName())
            );
        } else if ((inputValueType==null || inputValueType== ValueType.LONG)
                && (outputValueType==null || outputValueType==ValueType.INT)) {
            LongToIntFunction samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingLongIntSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new DiscreteLongIntSampler(icdSource, hashto);
            }
            resolvedFunctions.add(
                    new ResolvedFunction(
                            samplingFunction,
                            true,
                            usingCtor.getParameterTypes(), parameters,
                            Long.TYPE, Integer.TYPE,
                            getName())
            );
        } else if ((inputValueType==null || inputValueType==ValueType.INT)
                && (outputValueType==null || outputValueType==ValueType.INT)) {
            IntUnaryOperator samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingIntIntSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new DiscreteIntIntSampler(icdSource, hashto);
            }
            resolvedFunctions.add(
                    new ResolvedFunction(
                            samplingFunction,
                            true,
                            usingCtor.getParameterTypes(), parameters,
                            Integer.TYPE, Integer.TYPE,
                            getName()
                    )
            );
        }

        return resolvedFunctions;
    }

    @Override
    public String getName() {
        return "math4-dcurves";
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
        uniform_integer(UniformDiscreteDistribution.class), // uniform(0,100) (int min, int max)
        geometric(GeometricDistribution.class), // geometric(0.5) (double probability)
        poisson(PoissonDistribution.class), // poisson(5.0) (double avgrate)
        zipf(ZipfDistribution.class), // zipf(10000, 5.0) (int elements, double exponent)
        binomial(BinomialDistribution.class), // binomial(8,0.5) - (int trials, double probability)
        pascal(PascalDistribution.class); // pascal(10,0.33) - (int successes, double probability)

        private final Class<? extends DiscreteDistribution> distribution;

        IntegerDistribution(Class<? extends DiscreteDistribution> distribution) {
            this.distribution = distribution;
        }

        public Class<? extends DiscreteDistribution> getDistributionClass() {
            return distribution;
        }

        public static Optional<IntegerDistribution> optionalValueOf(String name) {
            for (IntegerDistribution integerDistribution : values()) {
                if (integerDistribution.toString().equals(name)) {
                    return Optional.of(integerDistribution);
                }
            }
            return Optional.empty();
        }
    }


}
