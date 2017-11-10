package io.virtdata.libraryimpl;

import com.google.auto.service.AutoService;
import com.google.common.collect.Sets;
import io.virtdata.api.DataMapperLibrary;
import io.virtdata.api.ValueType;
import io.virtdata.api.specs.SpecData;
import io.virtdata.core.AllDataMapperLibraries;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.libraryimpl.composers.MultiSpecData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <H2>Synopsis</H2>
 * <p>This library implements the ability to compose a lambda function from a sequence of other functions.
 * The resulting lambda will use the specialized primitive function interfaces, such as LongUnaryOperator, LongFunction, etc.
 * Where there are two functions which do not have matching input and output types, the most obvious conversion is made.
 * This means that while you are able to compose a LongUnaryOperator with a LongUnaryOperator for maximum
 * efficiency, you can also compose LongUnaryOperator with an IntFunction, and a best effort attempt will be made to
 * do a reasonable conversion in between.</p>
 *
 * <H2>Limitations</H2>
 * <P>Due to type erasure, it is not possible to know the generic type parameters for non-primitive functional types.
 * These include IntFunction&lt;?&gt;, LongFunction&lt;?&gt;, and in the worst case, Function&lt;?,?&gt;.
 * For these types, annotations are provided to better inform the runtime lambda compositor.</P>
 *
 * <H2>Multiple Paths</H2>
 * <P>The library allows for there to be multiple functions which match the spec, possibly because multiple
 * functions have the same name, but exist in different libraries or in different packages within the same library.
 * This means that the composer library must find a connecting path between the functions that can match at each stage,
 * disregarding all but one.</P>
 *
 * <H2>Path Finding</H2>
 * <P>The rule for finding the best path among the available functions is as follows, at each pairing between
 * adjacent stages of functions:</P>
 * <OL>
 * <li>The co-compatible output and input types between the functions are mapped. Functions sharing the co-compatible
 * types are kept in the list. Functions not sharing them are removed.</li>
 * <li>As long as functions can be removed in this way, the process iterates through the chain, starting again
 * at the front of the list.</li>
 * <li>When no functions can be removed due to lack of co-compatible types, each stage is selected according to
 * type preferences as represented in {@link ValueType}</li>
 *
 * <LI>If the next (outer) function does not have a compatible input type, move it down on the list.
 * If, after this step, there are functions which do have matching signatures, all others are removed.</LI>
 * </OL>
 */
@AutoService(DataMapperLibrary.class)
public class ComposerLibrary implements DataMapperLibrary {

    private final static String PREAMBLE = "compose ";
    private final static Logger logger = LoggerFactory.getLogger(DataMapperLibrary.class);

    @Override
    public String getLibraryName() {
        return "composer";
    }

    @Override
    public boolean canParseSpec(String spec) {
        return MultiSpecData.forOptionalSpec(PREAMBLE, spec).isPresent();
    }

    @Override
    public List<ResolvedFunction> resolveFunctions(String spec) {
        Optional<ResolvedFunction> resolvedFunction = resolveFunction(spec);
        ArrayList<ResolvedFunction> resolvedFunctions = new ArrayList<>();
        resolvedFunction.ifPresent(resolvedFunctions::add);
        return resolvedFunctions;
    }

    @SuppressWarnings("unchecked")
    public Optional<ResolvedFunction> resolveFunction(String specline) {
        MultiSpecData multiSpecData = MultiSpecData.forSpec(PREAMBLE, specline);

        multiSpecData.getResultType().orElseThrow(() -> new RuntimeException("composed mappers must have a -> type annotation at the end."));

        LinkedList<List<ResolvedFunction>> funcs = new LinkedList<>();
        LinkedList<Set<ValueType>> inputTypes = new LinkedList<>();
        inputTypes.add(new HashSet<ValueType>() {{
            add(multiSpecData.getResultType().get());
        }});

        for (int i = multiSpecData.getSpecs().size() - 1; i >= 0; i--) {
            SpecData specData = multiSpecData.getSpecs().get(i);
            List<ResolvedFunction> nodeFunctions = new LinkedList<>();
            for (ValueType valueType : inputTypes.peekFirst()) {
                String vectoredSpec = specData.forResultType(valueType).getCanonicalSpec();
                List<ResolvedFunction> vectoredFunctions = AllDataMapperLibraries.get().resolveFunctions(vectoredSpec);
                logger.trace("Found " + vectoredFunctions.size() + " vectored functions for " + vectoredSpec);
                if (vectoredFunctions.size() == 0) {
                    logger.warn("Falling back to sloppy conversion matching for " +
                            specData.getCanonicalSpec() + " in " + multiSpecData.getCanonicalSpec() +
                            " since no co-compatible type signatures were found.");
                    vectoredFunctions = AllDataMapperLibraries.get().resolveFunctions(specData.getCanonicalSpec());
                }
                if (vectoredFunctions.size() == 0) {
                    throw new RuntimeException("Unable to find any functions for " + specData.getCanonicalSpec());
                }
                nodeFunctions.addAll(vectoredFunctions);
            }
            funcs.addFirst(nodeFunctions);

            inputTypes.addFirst(new HashSet<>());
            for (ResolvedFunction nodeFunction : nodeFunctions) {
                inputTypes.peekFirst().add(nodeFunction.getFunctionType().getInputValueType());
            }
        }

        if (!inputTypes.peekFirst().contains(ValueType.LONG)) {
            throw new RuntimeException("There is no initial function which accepts a long input. Function chain, after type filtering: \n" +
                    summarize(funcs));
        }
        removeNonLongFunctions(funcs.getFirst());

        ValueType resultType = multiSpecData.getResultType().orElseThrow(() -> new RuntimeException("missing result type specifier"));
        List<ResolvedFunction> flattenedFuncs = optimizePath(funcs, resultType);

        FunctionAssembly assembly = new FunctionAssembly();
        logger.trace("composed summary: " + summarize(flattenedFuncs));
        boolean isThreadSafe = true;
        for (ResolvedFunction resolvedFunction : flattenedFuncs) {
            assembly.andThen(resolvedFunction.getFunctionObject());
            if (!resolvedFunction.isThreadSafe()) {
                isThreadSafe = false;
            }
        }

        ResolvedFunction composedFunction = assembly.getResolvedFunction(isThreadSafe);
        return Optional.of(composedFunction);
    }

    private void removeNonLongFunctions(List<ResolvedFunction> funcs)
    {
        List<ResolvedFunction> toRemove = new LinkedList<>();
        for (ResolvedFunction func : funcs)
        {
            if (func.getFunctionType().getInputValueType() != ValueType.LONG)
            {
                toRemove.add(func);
            }
        }
        if (toRemove.size() > 0 && toRemove.size() == funcs.size())
        {
            throw new RuntimeException("removeNonLongFunctions would remove all functions: " + funcs);
        }
        funcs.removeAll(toRemove);
    }

    private String summarize(List<ResolvedFunction> funcs) {
        return funcs.stream()
        .map(String::valueOf).collect(Collectors.joining("|"));
    }

    private String summarize(LinkedList<List<ResolvedFunction>> funcs) {
        List<List<String>> spans = new LinkedList<>();
        funcs.forEach(l -> spans.add(l.stream().map(String::valueOf).collect(Collectors.toList())));
        List<Optional<Integer>> widths = spans.stream().map(
                l -> l.stream().map(String::length).max(Integer::compare)).collect(Collectors.toList());
        String summary = spans.stream().map(
                l -> l.stream().map(String::valueOf).collect(Collectors.joining("|\n"))
        ).collect(Collectors.joining("\n"));
        return summary;
    }

    /**
     * <p>
     * Attempt path optimizations on each phase junction, considering the set of
     * candidate inner functions with the candidate outer functions.
     * This is an iterative process, that will keep trying until no apparent
     * progress is made. Each higher-precedence optimization strategy is used
     * iteratively as long as it makes progress and then the lower precedence
     * strategies are allowed to have their turn.
     * </p>
     * <p>
     * <p>It is considered an error if the strategies are unable to reduce each
     * phase down to a single preferred function. Therefore, the lowest precedence
     * strategy is the most aggressive, simply sorting the functions by basic
     * type preference and then removing all but the highest selected function.</p>
     *
     * @param funcs the list of candidate functions offered at each phase, in List&lt;List&gt; form.
     * @return a List of resolved functions that has been fully optimized
     */
    private List<ResolvedFunction> optimizePath(List<List<ResolvedFunction>> funcs, ValueType resultType) {
        List<ResolvedFunction> prevFuncs = null;
        List<ResolvedFunction> nextFuncs = null;
        int progress = -1;

        while (progress != 0) {
            progress = 0;
            progress += reduceByResultType(funcs.get(funcs.size() - 1), resultType);
            if (funcs.size() > 1) {
                for (List<ResolvedFunction> funcList : funcs) {
                    nextFuncs = funcList;
                    if (prevFuncs != null) {

                        progress += reduceByDirectTypes(prevFuncs, nextFuncs);
                        // attempt secondary strategy IFF higher precedence strategy failed
                        if (progress == 0) {
                            progress += reduceByPreferredTypes(prevFuncs, nextFuncs);
                        }
                    } // else first pass, prime pointers
                    prevFuncs = nextFuncs;
                }

            }
        }
        List<ResolvedFunction> optimized = funcs.stream().map(l -> l.get(0)).collect(Collectors.toList());
        return optimized;
    }

    private int reduceByResultType(List<ResolvedFunction> endFuncs, ValueType resultType) {
        int progressed = 0;
        LinkedList<ResolvedFunction> tmpList = new LinkedList<>(endFuncs);
        for (ResolvedFunction endFunc : tmpList) {
            if (!resultType.getValueClass().isAssignableFrom(endFunc.getResultClass())) {
                endFuncs.remove(endFunc);
                logger.trace("removed function '" + endFunc + "' because it does not yield type " + resultType);
                progressed++;
            }
        }
        if (endFuncs.size() == 0) {
            throw new RuntimeException("No end funcs were found which yield result type " + resultType);
        }
        return progressed;
    }

    private int reduceByPreferredTypes(List<ResolvedFunction> prevFuncs, List<ResolvedFunction> nextFuncs) {
        int progressed = 0;
        if (prevFuncs.size() > 1) {
            progressed += prevFuncs.size() - 1;
            Collections.sort(prevFuncs, ResolvedFunction.PREFERRED_TYPE_COMPARATOR);
            while (prevFuncs.size() > 1) {
                logger.trace("removing func " + prevFuncs.get(prevFuncs.size() - 1)
                        + " because " + prevFuncs.get(0) + " has more preferred types.");
                prevFuncs.remove(prevFuncs.size() - 1);
            }
        } else if (nextFuncs.size() > 1) {
            progressed += nextFuncs.size() - 1;
            Collections.sort(nextFuncs, ResolvedFunction.PREFERRED_TYPE_COMPARATOR);
            while (nextFuncs.size() > 1) {
                logger.trace("removing func " + nextFuncs.get(nextFuncs.size() - 1)
                        + " because " + nextFuncs.get(0) + " has more preferred types.");
                nextFuncs.remove(nextFuncs.size() - 1);
            }
        }
        return progressed;
    }

    /**
     * If there are direct type matches between the inner func and the outer func, then remove all
     * other outer funcs except the ones with direct matches.
     *
     * @param prevFuncs The list of candidate inner functions
     * @param nextFuncs The list of candidate outer functions
     * @return count of items removed
     */
    private int reduceByDirectTypes(List<ResolvedFunction> prevFuncs, List<ResolvedFunction> nextFuncs) {

        int progressed = 0;

        // Rule 1: If there are direct type matches, remove extraneous next funcs
        Set<Class<?>> outputs = getOutputs(prevFuncs);
        Set<Class<?>> inputs = getInputs(nextFuncs);
        Sets.SetView<Class<?>> directMatches = Sets.intersection(inputs, outputs);
        if (directMatches.size() > 0) {
            List<ResolvedFunction> toremove = new ArrayList<>();
            for (ResolvedFunction nextFunc : nextFuncs) {
                if (!directMatches.contains(nextFunc.getArgType())) {
                    logger.debug("removing next func: " + nextFunc + " because its input types are not satisfied by an previous func");
                    toremove.add(nextFunc);
                    progressed++;
                }
            }
            nextFuncs.removeAll(toremove);
        }
        return progressed;
    }

    private Set<Class<?>> getOutputs(List<ResolvedFunction> prevFuncs) {
        Set<Class<?>> outputs = new HashSet<>();
        for (ResolvedFunction func : prevFuncs) {
            outputs.add(func.getResultClass());
        }
        return outputs;
    }

    private Set<Class<?>> getInputs(List<ResolvedFunction> nextFuncs) {
        Set<Class<?>> inputs = new HashSet<>();
        for (ResolvedFunction nextFunc : nextFuncs) {
            inputs.add(nextFunc.getArgType());
        }
        return inputs;
    }

    @Override
    public List<String> getDataMapperNames() {
        List<String> genNames = new ArrayList<>();
        return new ArrayList<String>() {{
            add("compose");
        }};
    }

}
