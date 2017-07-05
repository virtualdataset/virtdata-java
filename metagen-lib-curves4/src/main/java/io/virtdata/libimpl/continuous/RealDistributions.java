package io.virtdata.libimpl.continuous;

import com.google.auto.service.AutoService;
import io.virtdata.api.DataMapperLibrary;
import io.virtdata.api.specs.SpecData;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.reflection.ConstructorResolver;
import io.virtdata.reflection.DeferredConstructor;
import org.apache.commons.math4.distribution.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
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
@AutoService(DataMapperLibrary.class)
public class RealDistributions implements DataMapperLibrary {

    private static final String MAPTO = "mapto_";
    private static final String HASHTO = "hashto_";
    private static final String COMPUTE = "compute_";
    private static final String INTERPOLATE = "interpolate_";

    public static LongToDoubleFunction forSpec(String spec) {
        Optional<ResolvedFunction> resolvedFunction = new RealDistributions().resolveFunction(spec);
        return resolvedFunction
                .map(ResolvedFunction::getFunctionObject)
                .map(f -> ((LongToDoubleFunction) f))
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
        return "math4-ccurves";
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
            RealDistribution.valueOf(distName);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        if (!canParseSpec(spec)) {
            return Optional.empty();
        }
        SpecData specData = SpecData.forSpec(spec);
        String funcName = specData.getFuncName();
        RealDistribution realDistribution =
                RealDistribution.valueOf(distributionNameFor(funcName));
        Class<? extends AbstractRealDistribution> distributionClass = realDistribution.getDistributionClass();
        DeferredConstructor<? extends AbstractRealDistribution> deferred =
                ConstructorResolver.resolve(distributionClass, specData.getArgs());
        AbstractRealDistribution distribution = deferred.construct();

        boolean interpolate = !funcName.contains(COMPUTE) || funcName.contains(INTERPOLATE);
        boolean hashto = !funcName.contains(MAPTO) || funcName.contains(HASHTO);

        DoubleUnaryOperator icdSource = new RealDistributionICDSource(distribution);
        LongToDoubleFunction samplingFunction = null;
        if (interpolate) {
            samplingFunction = new InterpolatingRealSampler(icdSource, 1000, hashto);
        } else {
            samplingFunction = new RealSampler(icdSource, hashto);
        }
        return Optional.of(new ResolvedFunction(samplingFunction, true));
    }

    @Override
    public List<ResolvedFunction> resolveFunctions(String specifier) {
        List<ResolvedFunction> resolvedList = new ArrayList<>();
        Optional<ResolvedFunction> resolvedFunction = resolveFunction(specifier);
        resolvedFunction.map(resolvedList::add);
        return resolvedList;
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
        constant_real(ConstantRealDistribution.class),
        beta(BetaDistribution.class),
        pareto(ParetoDistribution.class),
        gamma(GammaDistribution.class),
        uniform_real(UniformRealDistribution.class);

        private final Class<? extends AbstractRealDistribution> distribution;

        RealDistribution(Class<? extends AbstractRealDistribution> distribution) {
            this.distribution = distribution;
        }

        public Class<? extends AbstractRealDistribution> getDistributionClass() {
            return distribution;
        }
    }

}
