package io.virtdata.core;

import io.virtdata.api.Generator;
import io.virtdata.api.GeneratorLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllGenerators implements GeneratorLibrary {

    private static AllGenerators instance = new AllGenerators();
    private List<GeneratorLibrary> libraries = GeneratorLibraryFinder.getAll();

    private final static Logger logger = LoggerFactory.getLogger(AllGenerators.class);

    private AllGenerators() {
    }

    public static AllGenerators get() {
        return instance;
    }

    @Override
    public String getLibraryName() {
        return "ALL";
    }

    /**
     * This method modifies the usual logic of finding generators. This is to allow only libraries which
     * can parse the spec to have a chance to map the function objects internally.
     *
     * @param spec A specifier that describes the type and or parameterization of a new generator.
     * @param <T> generator type
     * @return an optional generator
     */
    @Override
    public <T> Optional<Generator<T>> getGenerator(String spec) {
        if (!canParseSpec(spec)) {
            throw new RuntimeException("No libraries could parse: " + spec);
        }

        List<ResolvedFunction> resolvedFunctions = resolveFunctions(spec);

        if (resolvedFunctions.size()!=1) {
            throw new RuntimeException("Found " + resolvedFunctions.size() + " resolved functions. This library " +
                    "expects there to be exactly 1");
        }
        Optional<ResolvedFunction> resolvedFunction = Optional.ofNullable(resolvedFunctions.get(0));
        Optional<Generator<T>> tGenerator = resolvedFunction
                .map(ResolvedFunction::getFunctionObject)
                .map(GeneratorFunctionMapper::map);
        return tGenerator;

    }

    /**
     * If any composed libraries can parse the spec, we just return that one.
     * @param spec a generator spec string
     * @return
     */
    @Override
    public boolean canParseSpec(String spec) {
        return libraries.stream().map(gl -> gl.canParseSpec(spec)).anyMatch(l-> l);
    }

    /**
     * This method modifies the usual logic of finding generators. This is to allow only libraries which
     * can parse the spec to have a chance to map the function objects internally.
     * @param spec A specifier that describes the type and or parameterization of a new generator.
     * @return a list of resolved functions
     */
    @Override
    public List<ResolvedFunction> resolveFunctions(String spec) {
        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();

        int parsingLibs=0;
        for (GeneratorLibrary library : libraries) {
            if (library.canParseSpec(spec)) {
                parsingLibs++;
                Optional<ResolvedFunction> resolvedFunction = library.resolveFunction(spec);
                if (resolvedFunction.isPresent()) {
                    resolvedFunctions.add(resolvedFunction.get());
                }
            }
        }
        if (parsingLibs==0) {
            throw new RuntimeException("No library could parse: " + spec);
        }
        return resolvedFunctions;
    }

    public Optional<ResolvedFunction> resolveFunction(String spec) {
        List<ResolvedFunction> resolvedFunctionList = resolveFunctions(spec);
        if (resolvedFunctionList.size() == 0) {
            logger.warn("Unable to find generator for spec '" + spec + "' in any libimpl, searched in " + toString());
            return Optional.empty();
        }
        if (resolvedFunctionList.size() > 1) {
            String resolvedNames = resolvedFunctionList.stream()
                    .map(r -> r.getClass().getCanonicalName())
                    .collect(Collectors.joining());
            logger.warn("Found more than one matching generator for spec '" + spec + "' : " + resolvedNames);
        }
        return Optional.of(resolvedFunctionList.get(0));
    }

    @Override
    public List<String> getGeneratorNames() {
        List<String> genNames = new ArrayList<>();
        for (GeneratorLibrary library : libraries) {
            List<String> libGenNames = library.getGeneratorNames().stream()
                    .map(genName -> library.getLibraryName() + "::" + genName)
                    .collect(Collectors.toList());
            genNames.addAll(libGenNames);
        }
        genNames.sort(Comparator.naturalOrder());
        return genNames;
    }

    public String toString() {
        return AllGenerators.class.getSimpleName() + ":"
                + libraries.stream().map(GeneratorLibrary::getLibraryName).collect(Collectors.joining(",", "[", "]"));
    }

}
