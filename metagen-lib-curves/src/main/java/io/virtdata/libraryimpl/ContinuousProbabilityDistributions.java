package io.virtdata.libraryimpl;

import com.google.auto.service.AutoService;
import io.virtdata.api.Generator;
import io.virtdata.api.GeneratorLibrary;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.core.SpecReader;
import io.virtdata.gen.generators.ToContinuousDistribution;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.random.EmpiricalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@AutoService(GeneratorLibrary.class)
public class ContinuousProbabilityDistributions implements GeneratorLibrary {

    private static final Logger logger = LoggerFactory.getLogger(ContinuousProbabilityDistributions.class);

    @Override
    public String getLibraryName() {
        return "cdist";
    }

    @Override
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        Optional<Class<? extends RealDistribution>> functionClass = resolveFunctionClass(spec);
        String[] generatorArgs = SpecReader.split(spec);
        if (!functionClass.isPresent()) {
            return Optional.empty();
        }
        generatorArgs[0] = functionClass.get().getCanonicalName();

        if (functionClass.isPresent()) {
            try {
                ToContinuousDistribution tcd = new ToContinuousDistribution(generatorArgs);
                ResolvedFunction resolvedFunction = new ResolvedFunction(tcd, this);
                return Optional.of(resolvedFunction);
            } catch (Exception e) {
                logger.error("Error instantiating generator:" + e.getMessage(), e);
                return Optional.empty();
            }
        } else {
            logger.debug("Generator class not found: " + spec);
            return Optional.empty();
        }
    }

    @Override
    public List<String> getGeneratorNames() {
        List<String> genNames = new ArrayList<>();
        return Arrays.stream(ContinuousDistributions.values()).map(Enum::toString).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Optional<Class<? extends RealDistribution>> resolveFunctionClass(String generatorSpec) {
        Class<Generator> generatorClass = null;
        String className = SpecReader.first(generatorSpec);
        try {
            ContinuousDistributions cdist = ContinuousDistributions.valueOf(className);
            logger.debug("Located continuous distribution:" + cdist.toString() + " for generator type: " + generatorSpec);
            return Optional.ofNullable(cdist.getDistClass());
        } catch (Exception e) {
            logger.debug("Unable to map continuous distribution class " + generatorSpec);
            return Optional.empty();
        }
    }

    private enum ContinuousDistributions {

        t(TDistribution.class),
        cauchy(CauchyDistribution.class),
        uniform(UniformRealDistribution.class),
        chi_squared(ChiSquaredDistribution.class),
        laplace(LaplaceDistribution.class),
        gumbel(GumbelDistribution.class),
        enumerated_real(EnumeratedRealDistribution.class),
        log_normal(LogNormalDistribution.class),
        weibull(WeibullDistribution.class),
        levy(LevyDistribution.class),
        gamma(GammaDistribution.class),
        beta(BetaDistribution.class),
        logistic(LogisticDistribution.class),
        pareto(ParetoDistribution.class),
        constant_real(ConstantRealDistribution.class),
        normal(NormalDistribution.class),
        exponential(ExponentialDistribution.class),
        empirical(EmpiricalDistribution.class),
        f(FDistribution.class),
        triangular(TriangularDistribution.class),
        nakagami(NakagamiDistribution.class);

        private final Class<? extends RealDistribution> distClass;

        ContinuousDistributions(Class<? extends RealDistribution> clazz) {
            this.distClass = clazz;
        }

        public Class<? extends RealDistribution> getDistClass() {
            return distClass;
        }

    }

}
