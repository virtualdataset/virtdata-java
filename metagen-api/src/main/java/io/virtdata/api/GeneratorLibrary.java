package io.virtdata.api;

import io.virtdata.api.specs.SpecData;
import io.virtdata.core.GeneratorFunctionMapper;
import io.virtdata.core.ResolvedFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * A GeneratorLibrary is an independently loadable library of generator functions.
 * To implement a GeneratorLibrary, you <em>must</em> implement one of
 * {@link #resolveFunction(String, Class)} or {@link #resolveFunctions(String, Class)}.
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
        if (canParseSpec(spec)) {
            Optional<ResolvedFunction> resolvedFunction = resolveFunction(spec);
            return resolvedFunction
                    .map(ResolvedFunction::getFunctionObject)
                    .map(GeneratorFunctionMapper::map);
        }
        return Optional.empty();
    }

    /**
     * Generator Libraries are required to test specifier strings in order to determine
     * whether or not the library could possibly find matching functions.
     * This allows varying types of specifiers to be used that are library specific,
     * allowing an ad-hoc form of syntax layering.
     *
     * @param spec a generator spec string
     * @return a tagged Specifier option if successful
     */
    boolean canParseSpec(String spec);

    default Optional<ResolvedFunction> resolveFunction(String spec) {
        SpecData specData = SpecData.forSpec(spec);
        List<ResolvedFunction> resolvedFunctions = resolveFunctions(spec);
        Optional<ValueType> resultType = specData.getResultType();
        if (resultType.isPresent() && resolvedFunctions.size() > 1) {
            int prefilter = resolvedFunctions.size();

            List<ResolvedFunction> previousFunctions = new ArrayList<ResolvedFunction>(resolvedFunctions);
            resolvedFunctions = resolvedFunctions.stream()
                    .filter(rf -> rf.getFunctionType().getReturnValueType() == resultType.get())
                    .collect(Collectors.toList());

            int postfilter = resolvedFunctions.size();
            if (prefilter > 0 && postfilter == 0) {
                String warning = "Before filtering for result type '" + resultType.get() + "', there"
                        + " were " + prefilter + " matching functions:" + previousFunctions.stream()
                        .map(Object::toString).collect(Collectors.joining(","));
                // TODO: Move this to a proper impl, remove default methods
                resolvedFunctions = previousFunctions;
            }

        }
        if (resolvedFunctions.size() == 0) {
            return Optional.empty();
        }
        if (resolvedFunctions.size() > 1) {
            Collections.sort(resolvedFunctions, ResolvedFunction.PREFERRED_TYPE_COMPARATOR);
            return Optional.of(resolvedFunctions.get(0));
        }
        return Optional.of(resolvedFunctions.get(0));
    }

    ;

//    /**
//     * <p>Metagen functions can be implemented in various functional types in Java. The FunctionResolver
//     * is the part of a libimpl that is responsible for finding and instantiating one of the allowed types.</p>
//     * <p>The {@link FunctionType} enum shows the allowed types of functions.</p>
//     *
//     * @param spec A specifier that describes the type and or parameterization of a new generator.
//     * @return an instance of a function object with associated metadata
//     */
//    default Optional<ResolvedFunction> resolveFunction(String spec, Class<?> desiredType) {
//        List<ResolvedFunction> resolvedFunctions = resolveFunctions(spec, desiredType);
//        if (resolvedFunctions.size() == 0) {
//            throw new RuntimeException("Unable to find any resolved functions for spec: " + spec);
//        }
//        if (resolvedFunctions.size() > 1) {
//            if (desiredType != null) {
//                resolvedFunctions = resolvedFunctions.stream()
//                        .filter(f -> FunctionType.valueOf(f).hasOutputClass(desiredType))
//                        .collect(Collectors.toList());
//            }
//            String found = resolvedFunctions.stream().map(o -> o.getClass().getCanonicalName()).collect(Collectors.joining());
//            throw new RuntimeException("There was more than one matching function for spec: '" + spec +
//                    "' : " + found + ". It is an error to return multiple generators at this level " +
//                    " in the library. Make sure your library knows how to select a single function " +
//                    "from multiple matches, if multiple matches are possible.");
//        }
//        return Optional.ofNullable(resolvedFunctions.get(0));
//    }

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
    List<String> getGeneratorNames();

}
