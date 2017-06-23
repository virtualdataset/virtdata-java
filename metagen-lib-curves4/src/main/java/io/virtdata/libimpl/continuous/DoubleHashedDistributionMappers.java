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
        mapped_levy(LevyDistribution.class),
        mapped_nakagami(NakagamiDistribution.class),
        mapped_triangular(TriangularDistribution.class),
        mapped_exponential(ExponentialDistribution.class),
        mapped_logistic(LogisticDistribution.class),
        mapped_enumerated_real(EnumeratedRealDistribution.class),
        mapped_laplace(LaplaceDistribution.class),
        mapped_log_normal(LogNormalDistribution.class),
        mapped_cauchy(CauchyDistribution.class),
        mapped_f(FDistribution.class),
        mapped_t(TDistribution.class),
        mapped_empirical(EmpiricalDistribution.class),
        mapped_normal(NormalDistribution.class),
        mapped_weibull(WeibullDistribution.class),
        mapped_chi_squared(ChiSquaredDistribution.class),
        mapped_gumbel(GumbelDistribution.class),
        mapped_constant_real(ConstantRealDistribution.class),
        mapped_beta(BetaDistribution.class),
        mapped_pareto(ParetoDistribution.class),
        mapped_gamma(GammaDistribution.class),
        mapped_uniform_real(UniformRealDistribution.class);

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
        if (specData.getFuncName().startsWith("mapped_")) {
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
