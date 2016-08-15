package io.virtdata.core;

import io.virtdata.api.Generator;
import io.virtdata.api.GeneratorLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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

    @Override
    public List<ResolvedFunction> resolveFunctions(String spec) {
        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();
        for (GeneratorLibrary library : this.libraries) {
            List<ResolvedFunction> rfs = library.resolveFunctions(spec);
            resolvedFunctions.addAll(rfs);
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
