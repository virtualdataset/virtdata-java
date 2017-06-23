package io.virtdata.libimpl.discrete;

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
import java.util.function.LongUnaryOperator;
import java.util.stream.Collectors;

@AutoService(DataMapperLibrary.class)
public class IntegerDistributions implements DataMapperLibrary {

    public static LongUnaryOperator forSpec(String spec) {
        Optional<ResolvedFunction> resolvedFunction = new IntegerDistributions().resolveFunction(spec);
        return resolvedFunction
                .map(ResolvedFunction::getFunctionObject)
                .map(f -> ((LongUnaryOperator) f)).orElseThrow(
                        () -> new RuntimeException("Invalid spec: " + spec)
                );
    }

    @Override
    public String getLibraryName() {
        return "math4-icurves";
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
        Class<? extends AbstractIntegerDistribution> distributionClass = libName.getDistributionClass();
        DeferredConstructor<? extends AbstractIntegerDistribution> deferred = ConstructorResolver.resolve(distributionClass, specData.getArgs());
        AbstractIntegerDistribution distribution = deferred.construct();
        if (specData.getFuncName().startsWith("mapto_")) {
            return Optional.of(new ResolvedFunction(new IMappedDistFunction(distribution)));
        } else {
            return Optional.of(new ResolvedFunction(new IHashedDistFunction(distribution)));
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

    private static enum LibName {
        enumerated_integer(EnumeratedIntegerDistribution.class),
        hypergeometric(HypergeometricDistribution.class),
        uniform(UniformIntegerDistribution.class),
        geometric(GeometricDistribution.class),
        poisson(PoissonDistribution.class),
        zipf(ZipfDistribution.class),
        binomial(BinomialDistribution.class),
        pascal(PascalDistribution.class),
        mapto_enumerated(EnumeratedIntegerDistribution.class),
        mapto_hypergeometric(HypergeometricDistribution.class),
        mapto_uniform(UniformIntegerDistribution.class),
        mapto_geometric(GeometricDistribution.class),
        mapto_poisson(PoissonDistribution.class),
        mapto_zipf(ZipfDistribution.class),
        mapto_binomial(BinomialDistribution.class),
        mapto_pascal(PascalDistribution.class);

        private final Class<? extends AbstractIntegerDistribution> distribution;

        LibName(Class<? extends AbstractIntegerDistribution> distribution) {
            this.distribution = distribution;
        }

        public Class<? extends AbstractIntegerDistribution> getDistributionClass() {
            return distribution;
        }
    }
}
