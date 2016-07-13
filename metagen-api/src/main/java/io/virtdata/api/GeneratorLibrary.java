package io.virtdata.api;

import io.virtdata.core.GeneratorFunctionMapper;
import io.virtdata.core.ResolvedFunction;

import java.util.List;
import java.util.Optional;

public interface GeneratorLibrary {

    /**
     * <p>Return the library name for this generator library, as it can be used in spec strings, etc.</p>
     *
     * @return Simple lower-case canonical library name
     */
    String getLibraryName();

    /**
     * <p>Find the implementation for and construct an instance of a generator, as described.</p>
     *
     * @param spec A specifier that describes the type and or parameterization of a new generator.
     * @param <T>  The result type produced by the generator.
     * @return An optional generator instances.
     */
    default <T> Optional<Generator<T>> getGenerator(String spec) {
        Optional<ResolvedFunction> resolvedFunction = resolveFunction(spec);
        return resolvedFunction
                .map(ResolvedFunction::getFunctionObject)
                .map(GeneratorFunctionMapper::map);
    }

    /**
     * <p>Metagen functions can be implemented in various functional types in Java. The FunctionResolver
     * is the part of a library that is responsible for finding and instantiating one of the allowed types.</p>
     * <p>The {@link FunctionType} enum shows the allowed types of functions.</p>
     *
     * @param spec A specifier that describes the type and or parameterization of a new generator.
     * @return an instance of a function object with associated metadata
     */
    Optional<ResolvedFunction> resolveFunction(String spec);

    /**
     * <p>Get the list of known generator names.</p>
     * @return list of generator names that can be used in generator specs
     */
    List<String> getGeneratorNames();

}
