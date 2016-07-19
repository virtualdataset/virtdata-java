package io.virtdata.libimpl;

import com.google.auto.service.AutoService;
import io.virtdata.api.Generator;
import io.virtdata.api.GeneratorLibrary;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.core.SpecReader;
import io.virtdata.mappers.mapped_discrete.IDistMapper;
import org.apache.commons.math3.distribution.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@AutoService(GeneratorLibrary.class)
public class IDistMappedLibrary implements GeneratorLibrary {

    private static final Logger logger = LoggerFactory.getLogger(IDistMappedLibrary.class);

    @Override
    public String getLibraryName() {
        return "mapto_idist";
    }

    @Override
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        Optional<Class<? extends IntegerDistribution>> functionClass = resolveFunctionClass(spec);
        String[] generatorArgs = SpecReader.split(spec);
        if (!functionClass.isPresent()) {
            return Optional.empty();
        }
        generatorArgs[0] = functionClass.get().getCanonicalName();

        if (functionClass.isPresent()) {
            try {
                IDistMapper tcd = new IDistMapper(generatorArgs);
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
        return Arrays.stream(DiscreteDistributions.values()).map(Enum::toString).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Optional<Class<? extends IntegerDistribution>> resolveFunctionClass(String generatorSpec) {
        Class<Generator> generatorClass = null;
        String className = SpecReader.first(generatorSpec);
        try {
            DiscreteDistributions ddist = DiscreteDistributions.valueOf(className);
            logger.debug("Located continuous distribution:" + ddist.toString() + " for generator type: " + generatorSpec);
            return Optional.ofNullable(ddist.getDistClass());
        } catch (Exception e) {
            logger.debug("Unable to map continuous distribution class " + generatorSpec);
            return Optional.empty();
        }
    }

    private enum DiscreteDistributions {

        mapto_pascal(PascalDistribution.class),
        mapto_binomial(BinomialDistribution.class),
        mapto_zipf(ZipfDistribution.class),
        mapto_hypergeometric(HypergeometricDistribution.class),
        mapto_uniform_integer(UniformIntegerDistribution.class),
        mapto_enumerated_integer(EnumeratedIntegerDistribution.class),
        mapto_geometric(GeometricDistribution.class),
        mapto_poisson(PoissonDistribution.class);

        private final Class<? extends IntegerDistribution> distClass;

        DiscreteDistributions(Class<? extends IntegerDistribution> clazz) {
            this.distClass = clazz;
        }

        public Class<? extends IntegerDistribution> getDistClass() {
            return distClass;
        }

    }

}
