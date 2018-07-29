package io.virtdata.core;

import io.virtdata.api.DataMapper;
import io.virtdata.ast.VirtDataFlow;
import io.virtdata.parser.VirtDataDSL;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VirtData {

    /**
     * Create a bindings template from the pair-wise names and specifiers.
     * Each even-numbered (starting with zero) argument is a binding name,
     * and each odd-numbered (starting with one) argument is a binding spec.
     *
     * @param namesAndSpecs names and specs in "name", "spec", ... form
     * @return A bindings template that can be used to resolve a bindings instance
     */
    public static BindingsTemplate getTemplate(String... namesAndSpecs) {
        if ((namesAndSpecs.length%2)!=0) {
            throw new RuntimeException(
                    "args must be in 'name','spec', pairs. " +
                            "This can't be true for " + namesAndSpecs.length + "elements.");
        }
        Map<String,String> specmap = new HashMap<>();
        for (int i = 0; i < namesAndSpecs.length; i+=2) {
            specmap.put(namesAndSpecs[i],namesAndSpecs[i+1]);
        }
        return getTemplate(specmap);
    }

    /**
     * Create a bindings template from the provided map, ensuring that
     * the syntax of the bindings specs is parsable first.
     * @param namedBindings The named bindings map
     * @return a bindings template
     */
    public static BindingsTemplate getTemplate(Map<String, String> namedBindings) {

        for(String bindingSpec : namedBindings.values()) {
            VirtDataDSL.ParseResult parseResult = VirtDataDSL.parse(bindingSpec);
            if (parseResult.throwable!=null) {
                throw new RuntimeException(parseResult.throwable);
            }
        }
        return new BindingsTemplate(namedBindings);
    }

    /**
     * Instantiate an optional data mapping function if possible.
     * @param flowSpec The VirtData specifier for the mapping function
     * @param <T> The parameterized return type of the function
     * @return An optional function which will be empty if the function could not be resolved.
     */
    public static <T> Optional<DataMapper<T>> getOptionalMapper(String flowSpec) {

        flowSpec= CompatibilityFixups.fixup(flowSpec);

        VirtDataDSL.ParseResult parseResult = VirtDataDSL.parse(flowSpec);
        if (parseResult.throwable!=null) {
            throw new RuntimeException(parseResult.throwable);
        }
        VirtDataFlow flow = parseResult.flow;
        VirtDataComposer composer = new VirtDataComposer();
        Optional<ResolvedFunction> resolvedFunction = composer.resolveFunctionFlow(flow);
        return resolvedFunction.map(ResolvedFunction::getFunctionObject).map(DataMapperFunctionMapper::map);
    }

    /**
     * Instantiate a data mapping function, or throw an exception.
     * @param flowSpec The VirtData specifier for the mapping function
     * @param <T> The parameterized return type of the function
     * @return A data mapping function
     * @throws RuntimeException if the function could not be resolved
     */
    public static <T> DataMapper<T> getMapper(String flowSpec) {
        Optional<DataMapper<T>> optionalMapper = getOptionalMapper(flowSpec);
        return optionalMapper.orElseThrow(() -> new RuntimeException("Unable to find mapper: " + flowSpec));
    }

    /**
     * Instantiate a data mapping function of the specified type, or throw an error.
     * @param flowSpec The VirtData flow specifier for the function to be returned
     * @param clazz The class of the data mapping function return type
     * @param <T> The parameterized class of the data mapping return type
     * @return A new data mapping function.
     * @throws RuntimeException if the function could not be resolved
     */
    public static <T> DataMapper<T> getMapper(String flowSpec, Class<? extends T> clazz) {
        Optional<DataMapper<T>> dataMapper = getOptionalMapper(flowSpec);
        return dataMapper.orElseThrow(() -> new RuntimeException("Unable to find mapper: " + flowSpec));
    }

    public static <T> Optional<DataMapper<T>> getOptionalMapper(String flowSpec, Class<? extends T> clazz) {
        Optional<DataMapper<T>> dataMapper = getOptionalMapper(flowSpec);
        return dataMapper;
    }

}
