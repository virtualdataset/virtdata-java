package io.virtdata.api;

import io.virtdata.api.types.FunctionType;
import io.virtdata.core.GeneratorFunctionMapper;
import io.virtdata.core.ResolvedFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * A GeneratorLibrary is an independently loadable library of generator functions.
 * To implement a GeneratorLibrary, you <em>must</em> implement one of
 * {@link #resolveFunction(String)} or {@link #resolveFunctions(String)}.
 * <p>
 * </P>
 */
public interface GeneratorLibrary {

    /**
     * <p>Return the libimpl name for this generator libimpl, as it can be used in spec strings, etc.</p>
     *
     * @return Simple lower-case canonical libimpl name
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
     * is the part of a libimpl that is responsible for finding and instantiating one of the allowed types.</p>
     * <p>The {@link FunctionType} enum shows the allowed types of functions.</p>
     *
     * @param spec A specifier that describes the type and or parameterization of a new generator.
     * @return an instance of a function object with associated metadata
     */
    default Optional<ResolvedFunction> resolveFunction(String spec) {
        List<ResolvedFunction> resolvedFunctions = resolveFunctions(spec);
        if (resolvedFunctions.size() == 0) {
            throw new RuntimeException("Unable to find any resolved functions for spec: " + spec);
        }
        if (resolvedFunctions.size() > 1) {
            String found = resolvedFunctions.stream().map(o -> o.getClass().getCanonicalName()).collect(Collectors.joining());
            throw new RuntimeException("There was more than one matching function for spec: '" + spec +
                    "' : " + found);
        }
        return Optional.ofNullable(resolvedFunctions.get(0));
    }

    /**
     * This is like {@link #resolveFunction(String)}, except that it will return zero or more
     * matching functions without returning an error.
     *
     * @param spec A specifier that describes the type and parameterization of a new generator.
     * @return a list of function instances
     */
    default List<ResolvedFunction> resolveFunctions(String spec) {
        List<ResolvedFunction> funcs = new ArrayList<ResolvedFunction>();
        Optional<ResolvedFunction> resolvedFunction = resolveFunction(spec);
        if (resolvedFunction.isPresent()) {
            funcs.add(resolvedFunction.get());
        }
        return funcs;
    }

    ;

    /**
     * <p>Get the list of known generator names.</p>
     *
     * @return list of generator names that can be used in generator specs
     */
    List<String> getGeneratorNames();

}
