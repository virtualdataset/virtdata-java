package io.virtdata.core;

import io.virtdata.api.Generator;
import io.virtdata.api.GeneratorLibrary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllGenerators implements GeneratorLibrary {

    private static AllGenerators instance = new AllGenerators();
    private List<GeneratorLibrary> libraries = GeneratorLibraryFinder.getAll();

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
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        Optional<Generator<?>> generator = null;
        for (GeneratorLibrary library : libraries) {
            Optional<ResolvedFunction> resolvedFunction = library.resolveFunction(spec);
            if (resolvedFunction.isPresent()) {
                return resolvedFunction;
            }
        }
        throw new RuntimeException("Unable to find generator for spec '" + spec + "' in any libimpl, searched in " + toString());
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
                + libraries.stream().map(GeneratorLibrary::getLibraryName).collect(Collectors.joining(",","[","]"));
    }

}
