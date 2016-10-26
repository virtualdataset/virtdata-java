package io.virtdata.libimpl;

import com.google.auto.service.AutoService;
import io.virtdata.api.DataMapper;
import io.virtdata.api.DataMapperLibrary;
import io.virtdata.api.specs.SpecData;
import io.virtdata.core.ResolvedFunction;
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
@AutoService(DataMapperLibrary.class)
public class IDistHashedLibrary implements DataMapperLibrary {

    private static final Logger logger = LoggerFactory.getLogger(IDistHashedLibrary.class);

    @Override
    public String getLibraryName() {
        return "hashto_idist";
    }

    @Override
    public List<ResolvedFunction> resolveFunctions(String spec) {
        List<ResolvedFunction> resolved = new ArrayList<>();
        SpecData specData = SpecData.forSpec(spec);

        Optional<Class<? extends IntegerDistribution>> functionClass = resolveFunctionClass(spec);

        if (functionClass.isPresent()) {
            String[] dataMapperArgs = specData.getFuncAndArgs();
            dataMapperArgs[0] = functionClass.get().getCanonicalName();
            try {
                IDistMapper tcd = new IDistMapper(dataMapperArgs);
                ResolvedFunction resolvedFunction = new ResolvedFunction(tcd, this);
                resolved.add(resolvedFunction);
            } catch (Exception e) {
                logger.error("Error instantiating data mapper:" + e.getMessage(), e);
            }
        } else {
            logger.debug("Integer Distribution class not found: " + spec);
        }
        return resolved;
    }

    @Override
    public List<String> getDataMapperNames() {
        List<String> genNames = new ArrayList<>();
        return Arrays.stream(DiscreteDistributions.values()).map(Enum::toString).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Optional<Class<? extends IntegerDistribution>> resolveFunctionClass(String generatorSpec) {
        Class<DataMapper> generatorClass = null;
        SpecData specData = SpecData.forSpec(generatorSpec);
        String className = specData.getFuncName();

        try {
            DiscreteDistributions ddist = DiscreteDistributions.valueOf(className);
            logger.debug("Located continuous distribution:" + ddist.toString() + " for data mapper type: " + generatorSpec);
            return Optional.ofNullable(ddist.getDistClass());
        } catch (Exception e) {
            logger.debug("Unable to map continuous distribution class " + generatorSpec);
            return Optional.empty();
        }
    }

    private enum DiscreteDistributions {

        pascal(PascalDistribution.class),
        binomial(BinomialDistribution.class),
        zipf(ZipfDistribution.class),
        hypergeometric(HypergeometricDistribution.class),
        uniform_integer(UniformIntegerDistribution.class),
        enumerated_integer(EnumeratedIntegerDistribution.class),
        geometric(GeometricDistribution.class),
        poisson(PoissonDistribution.class);

        private final Class<? extends IntegerDistribution> distClass;

        DiscreteDistributions(Class<? extends IntegerDistribution> clazz) {
            this.distClass = clazz;
        }

        public Class<? extends IntegerDistribution> getDistClass() {
            return distClass;
        }

    }

    @Override
    public boolean canParseSpec(String spec) {
        return SpecData.forOptionalSpec(spec).isPresent();
    }

    @Override
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        List<ResolvedFunction> resolvedFunctions = resolveFunctions(spec);
        if (resolvedFunctions.size()==1) {
            return Optional.of(resolvedFunctions.get(0));
        }
        return Optional.empty();
    }

}
