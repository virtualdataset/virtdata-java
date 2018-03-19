package io.virtdata.libimpl.continuous;

import com.google.auto.service.AutoService;
import io.virtdata.api.ValueType;
import io.virtdata.api.VirtDataFunctionLibrary;
import io.virtdata.ast.FunctionCall;
import io.virtdata.ast.MetagenFlow;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.libimpl.discrete.IntegerDistributions;
import io.virtdata.parser.VirtDataDSL;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.math4.distribution.EmpiricalDistribution;
import org.apache.commons.math4.distribution.EnumeratedRealDistribution;
import org.apache.commons.statistics.distribution.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntToDoubleFunction;
import java.util.function.LongToDoubleFunction;

/**
 * <p>This mapper provides inverse cumulative distribution sampling for real-valued
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
 * <li>normal(10.0,1.0) - use interpolation and hashing to simulate sampling from
 * the normal distribution, with a median value of 10.0 and a stddev of 1.0. Because no
 * qualifiers were included as explained above, hashto_ and interpolate_
 * were implied.</li>
 * <li>mapto_normal(10,1.0) - Same distribution as above, but use ICDF mapping
 * instead of hashing. Continue to use interpolation as implied</li>
 * <li>compute_normal(10,1.0) - Same distribution as above, but enforce
 * non-interpolated mode. Since mapto_ and hashto_ weren't provided,
 * hashto_ is implied.</li>
 * <li>mapto_compute_normal(10,1.0) - This will be very slow. This forces
 * computation of a harmonic series over N elements for <em>every single
 * access.</em> Interpolation is usually the better way.</li>
 * </ul>
 */
@AutoService(VirtDataFunctionLibrary.class)
public class RealDistributions implements VirtDataFunctionLibrary {

    private static final String MAPTO = "mapto_";
    private static final String HASHTO = "hashto_";
    private static final String COMPUTE = "compute_";
    private static final String INTERPOLATE = "interpolate_";

    public static LongToDoubleFunction forSpec(String spec) {

        VirtDataDSL.ParseResult parseResult = VirtDataDSL.parse(spec);
        if (parseResult.throwable!=null) {
            throw new RuntimeException(parseResult.throwable);
        }
        MetagenFlow flow = parseResult.flow;
        if (flow.getExpressions().size()>1) {
            throw new RuntimeException("Unable to parse flows in " + IntegerDistributions.class);
        }
        FunctionCall call = flow.getLastExpression().getCall();
        Class<?> inType = Optional.ofNullable(call.getInputType()).map(ValueType::valueOfClassName).map(ValueType::getValueClass).orElse(null);
        Class<?> outType = Optional.ofNullable(call.getOutputType()).map(ValueType::valueOfClassName).map(ValueType::getValueClass).orElse(null);
        if (inType!=null && inType!=long.class) {
            throw new RuntimeException("This only supports long for input.");
        }
        if (outType!=null && outType!=double.class) {
            throw new RuntimeException("This only supports double fo routput.");
        }
        inType = (inType==null ? long.class : inType);
        outType = (outType==null ? int.class : outType);

        List<ResolvedFunction> resolvedFunctions = new RealDistributions().resolveFunctions(
                outType, inType, call.getFunctionName(), call.getArguments()
        );

        if (resolvedFunctions.size()>1) {
            throw new RuntimeException("Found " + resolvedFunctions.size() + " implementations, be more specific with" +
                    "input or output qualifiers as in int -> or -> long");
        }

        return ((LongToDoubleFunction) resolvedFunctions.get(0).getFunctionObject());
    }

    private static String distributionNameFor(String specName) {
        return specName.replaceAll(COMPUTE, "")
                .replaceAll(INTERPOLATE, "")
                .replaceAll(MAPTO, "")
                .replaceAll(HASHTO, "");
    }

    @Override
    public String getName() {
        return "math4-ccurves";
    }

    @Override
    public List<ResolvedFunction> resolveFunctions(Class<?> outputClass, Class<?> inputClass, String functionName, Object... parameters) {
        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();

        ValueType inputType = inputClass==null ? null : ValueType.valueOfAssignableClass(inputClass);

        Optional<RealDistribution> optionalRealDistribution =
                RealDistribution.optionalValueOf(distributionNameFor(functionName));
        if (!optionalRealDistribution.isPresent()) {
            return resolvedFunctions;
        }
        Class<? extends ContinuousDistribution> distributionClass = optionalRealDistribution.get().getDistributionClass();

        Class<?>[] initTypes = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            initTypes[i] = parameters[i].getClass();
        }
        Constructor<? extends ContinuousDistribution> usingCtor =
                ConstructorUtils.getMatchingAccessibleConstructor(distributionClass, initTypes);

        ContinuousDistribution distribution;
        try {
            distribution = ConstructorUtils.invokeConstructor(distributionClass, parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        boolean interpolate = !functionName.contains(COMPUTE) || functionName.contains(INTERPOLATE);
        boolean hashto = !functionName.contains(MAPTO) || functionName.contains(HASHTO);

        DoubleUnaryOperator icdSource = new RealDistributionICDSource(distribution);

        if (inputType==ValueType.LONG || inputType==null) {
            LongToDoubleFunction samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingLongDoubleSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new RealLongDoubleSampler(icdSource, hashto);
            }
            resolvedFunctions.add(
                    new ResolvedFunction(
                            samplingFunction,
                            true,
                            usingCtor.getParameterTypes(), parameters,
                            Long.TYPE, Double.TYPE,
                            getName()
                    )
            );
        }

        if (inputType==ValueType.INT || inputType==null) {
            IntToDoubleFunction samplingFunction = null;
            if (interpolate) {
                samplingFunction = new InterpolatingIntDoubleSampler(icdSource, 1000, hashto);
            } else {
                samplingFunction = new RealIntDoubleSampler(icdSource, hashto);
            }
            resolvedFunctions.add(
                    new ResolvedFunction(
                            samplingFunction,
                            true,
                            usingCtor.getParameterTypes(), parameters,
                            Integer.TYPE, Double.TYPE,
                            getName())
            );
        }

        return resolvedFunctions;
    }

    @Override
    public List<String> getDataMapperNames() {
        List<String> names = new ArrayList<>();
        Arrays.stream(RealDistribution.values()).map(String::valueOf).forEach(
                n -> {
                    names.add(n);
                    names.add("mapto_" + n);
                    names.add("mapto_compute_" + n);
                    names.add("compute_" + n);
                }
        );
        return names;
    }


    private static enum RealDistribution {
        // https://en.wikipedia.org/wiki/L%C3%A9vy_distribution
        // double location, double scale (u, c)
        levy(LevyDistribution.class),
        nakagami(NakagamiDistribution.class),
        triangular(TriangularDistribution.class),
        exponential(ExponentialDistribution.class),
        logistic(LogisticDistribution.class),
        enumerated_real(EnumeratedRealDistribution.class),
        laplace(LaplaceDistribution.class),
        log_normal(LogNormalDistribution.class),
        cauchy(CauchyDistribution.class),
        f(FDistribution.class),
        t(TDistribution.class),
        empirical(EmpiricalDistribution.class),
        normal(NormalDistribution.class),
        weibull(WeibullDistribution.class),
        chi_squared(ChiSquaredDistribution.class),
        gumbel(GumbelDistribution.class),
//        constant_real(ConstantRealDistribution.class),
        beta(BetaDistribution.class),
        pareto(ParetoDistribution.class),
        gamma(GammaDistribution.class),
        uniform_real(UniformContinuousDistribution.class);

        private final Class<? extends ContinuousDistribution> distribution;

        RealDistribution(Class<? extends ContinuousDistribution> distribution) {
            this.distribution = distribution;
        }

        public Class<? extends ContinuousDistribution> getDistributionClass() {
            return distribution;
        }

        public static Optional<RealDistribution> optionalValueOf(String name) {
            for (RealDistribution realDistribution : values()) {
                if (realDistribution.toString().equals(name)) {
                    return Optional.of(realDistribution);
                }
            }
            return Optional.empty();
        }
    }

}
