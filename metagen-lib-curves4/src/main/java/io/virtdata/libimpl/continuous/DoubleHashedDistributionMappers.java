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
import java.util.stream.Collectors;

@AutoService(DataMapperLibrary.class)
public class DoubleHashedDistributionMappers implements DataMapperLibrary {

    private static enum LibName {
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
        uniform_real(UniformRealDistribution.class),
        mapto_levy(LevyDistribution.class),
        mapto_nakagami(NakagamiDistribution.class),
        mapto_triangular(TriangularDistribution.class),
        mapto_exponential(ExponentialDistribution.class),
        mapto_logistic(LogisticDistribution.class),
        mapto_enumerated_real(EnumeratedRealDistribution.class),
        mapto_laplace(LaplaceDistribution.class),
        mapto_log_normal(LogNormalDistribution.class),
        mapto_cauchy(CauchyDistribution.class),
        mapto_f(FDistribution.class),
        mapto_t(TDistribution.class),
        mapto_empirical(EmpiricalDistribution.class),
        mapto_normal(NormalDistribution.class),
        mapto_weibull(WeibullDistribution.class),
        mapto_chi_squared(ChiSquaredDistribution.class),
        mapto_gumbel(GumbelDistribution.class),
        mapto_constant_real(ConstantRealDistribution.class),
        mapto_beta(BetaDistribution.class),
        mapto_pareto(ParetoDistribution.class),
        mapto_gamma(GammaDistribution.class),
        mapto_uniform_real(UniformRealDistribution.class);

        private final Class<? extends AbstractRealDistribution> distribution;

        LibName(Class<? extends AbstractRealDistribution> distribution) {
            this.distribution = distribution;
        }

        public Class<? extends AbstractRealDistribution> getDistributionClass() {
            return distribution;
        }
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
            LibName.valueOf(specData.getFuncName());
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
        LibName libName = LibName.valueOf(specData.getFuncName());
        Class<? extends AbstractRealDistribution> distributionClass = libName.getDistributionClass();
        DeferredConstructor<? extends AbstractRealDistribution> deferred = ConstructorResolver.resolve(distributionClass, specData.getArgs());
        AbstractRealDistribution distribution = deferred.construct();
        if (specData.getFuncName().startsWith("mapto_")) {
            return Optional.of(new ResolvedFunction(new CMappedDistFunction(distribution)));
        } else {
            return Optional.of(new ResolvedFunction(new CHashedDistFunction(distribution)));
        }
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
        return Arrays.stream(LibName.values()).map(String::valueOf).collect(Collectors.toList());
    }
}
