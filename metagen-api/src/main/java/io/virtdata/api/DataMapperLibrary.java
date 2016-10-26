package io.virtdata.api;

import io.virtdata.core.DataMapperFunctionMapper;
import io.virtdata.core.ResolvedFunction;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * A DataMapperLibrary is an independently loadable library of generator functions.
 * </p>
 */
public interface DataMapperLibrary {

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
    default <T> Optional<DataMapper<T>> getDataMapper(String spec) {
        if (canParseSpec(spec)) {
            Optional<ResolvedFunction> resolvedFunction = resolveFunction(spec);
            return resolvedFunction
                    .map(ResolvedFunction::getFunctionObject)
                    .map(DataMapperFunctionMapper::map);
        }
        return Optional.empty();
    }

    /**
     * DataMapper Libraries are required to test specifier strings in order to determine
     * whether or not the library could possibly find matching functions.
     * This allows varying types of specifiers to be used that are library specific,
     * allowing an ad-hoc form of syntax layering.
     *
     * @param spec a generator spec string
     * @return a tagged Specifier option if successful
     */
    boolean canParseSpec(String spec);

    Optional<ResolvedFunction> resolveFunction(String spec);

    /**
     * @param specifier A specifier that describes the type and parameterization of a new generator.
     *                  The type of specifier will be specific to your library implementation. You can use SpecData by default.
     * @return a list of function instances
     */
    List<ResolvedFunction> resolveFunctions(String specifier);

    /**
     * <p>Get the list of known generator names.</p>
     *
     * @return list of generator names that can be used in generator specs
     */
    List<String> getDataMapperNames();

}
