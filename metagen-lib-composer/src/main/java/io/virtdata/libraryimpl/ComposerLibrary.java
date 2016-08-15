package io.virtdata.libraryimpl;

import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.virtdata.api.GeneratorLibrary;
import io.virtdata.api.types.ValueType;
import io.virtdata.core.AllGenerators;
import io.virtdata.core.ResolvedFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <H2>Synopsis</H2>
 * <p>
 * This library implements the ability to compose a lambda function from a sequence of other functions.
 * The resulting lambda will use the specialized primitive function interfaces, such as LongUnaryOperator, LongFunction, etc.
 * Where there are two functions which do not have matching input and output types, the most obvious conversion is made.
 * This means that while you are able to compose a LongUnaryOperator with a LongUnaryOperator for maximum
 * efficiency, you can also compose LongUnaryOperator with an IntFunction, and a best effort attempt will be made to
 * do a reasonable conversion in between.</p>
 * <p>
 * <H2>Limitations</H2>
 * <P>Due to type erasure, it is not possible to know the generic type parameters for non-primitive functional types.
 * These include IntFunction&lt;?&gt;, LongFunction&lt;?&gt;, and in the worst case, Function&lt;?,?&gt;.
 * For these types, annotations are provided to better inform the runtime lambda compositor.</P>
 * <p>
 * <H2>Multiple Paths</H2>
 * <P>The library allows for there to be multiple functions which match the spec, possibly because multiple
 * functions have the same name, but exist in different libraries or in different packages within the same library.
 * This means that the composer library must find a connecting path between the functions that can match at each stage,
 * disregarding all but one.</P>
 * <p>
 * <H2>Path Finding</H2>
 * <P>The rule for finding the best path among the available functions is as follows, at each step:</P>
 * <OL>
 * <LI>If the next (outer) function does not have a compatible input type, move it down on the list.
 * If, after this step, there are functions which do have matching signatures, all others are removed.</LI>
 * <LI></LI>
 * </OL>
 */
@AutoService(GeneratorLibrary.class)
public class ComposerLibrary implements GeneratorLibrary {

    private final static Logger logger = LoggerFactory.getLogger(GeneratorLibrary.class);

    @Override
    public String getLibraryName() {
        return "composer";
    }

    @Override
    public List<ResolvedFunction> resolveFunctions(String spec) {
        Optional<ResolvedFunction> resolvedFunction = resolveFunction(spec);
        ArrayList<ResolvedFunction> resolvedFunctions = new ArrayList<>();
        if (resolvedFunction.isPresent()) {
            resolvedFunctions.add(resolvedFunction.get());
        }
        return resolvedFunctions;
    }

    @SuppressWarnings("unchecked")
    public Optional<ResolvedFunction> resolveFunction(String specline) {
        if (!specline.startsWith("compose ")) {
            return Optional.empty();
        }

        String[] specs = specline.substring("compose ".length()).split(" ");

        List<List<ResolvedFunction>> funcs = new ArrayList<>();
        for (String spec : specs) {
            List<ResolvedFunction> nodeFunctions = AllGenerators.get().resolveFunctions(spec);
            funcs.add(nodeFunctions);
        }

        List<ResolvedFunction> flattendFuncs = optimizePath(funcs);

        FunctionAssembly fassy = new FunctionAssembly();
        for (ResolvedFunction resolvedFunction : flattendFuncs) {
            fassy.andThen(resolvedFunction.getFunctionObject());
        }

        ResolvedFunction composedFunction = fassy.getResolvedFunction();
        return Optional.of(composedFunction);
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
     *
     * <p>It is considered an error if the strategies are unable to reduce each
     * phase down to a single preferred function. Therefore, the lowest precedence
     * strategy is the most aggressive, simply sorting the functions by basic
     * type preference and then removing all but the highest selected function.</p>
     *
     * @param funcs the list of candidate functions offered at each phase, in List&lt;List&gt; form.
     * @return a List of resolved functions that has been fully optimized
     */
    private List<ResolvedFunction> optimizePath(List<List<ResolvedFunction>> funcs) {
        List<ResolvedFunction> prevFuncs = null;
        List<ResolvedFunction> nextFuncs = null;
        int progress = -1;

        while (progress != 0) {
            progress = 0;
            for (List<ResolvedFunction> funcList : funcs) {
                nextFuncs = funcList;
                if (prevFuncs != null) {
                    progress += reduceByDirectTypes(prevFuncs, nextFuncs);
                }
                // attempt secondary strategy IFF higher precedence strategy failed
                if (progress == 0) {
                    progress += reduceByPreferredTypes(prevFuncs, nextFuncs);
                }
                prevFuncs = nextFuncs;
            }
        }
        return funcs.stream().map(l -> l.get(0)).collect(Collectors.toList());
    }

    /**
     * Compare two ResolvedFunctions by preferred input type and then by preferred output type.
     */
    private static class PreferredTypeComparator implements Comparator<ResolvedFunction> {

        @Override
        public int compare(ResolvedFunction o1, ResolvedFunction o2) {
            ValueType iv1 = ValueType.valueOf(o1.getArgType());
            ValueType iv2 = ValueType.valueOf(o2.getArgType());
            int inputComparison = iv1.compareTo(iv2);
            if (inputComparison != 0) {
                return inputComparison;
            }
            iv1 = ValueType.valueOf(o1.getReturnType());
            iv2 = ValueType.valueOf(o2.getReturnType());
            return iv1.compareTo(iv2);
        }
    }

    private static Comparator<ResolvedFunction> preferredTypeComparator = new PreferredTypeComparator();

    private int reduceByPreferredTypes(List<ResolvedFunction> prevFuncs, List<ResolvedFunction> nextFuncs) {
        int progressed = 0;
        if (prevFuncs.size() > 1) {
            progressed += prevFuncs.size() - 1;
            Collections.sort(prevFuncs, preferredTypeComparator);
            while (prevFuncs.size() > 1) {
                prevFuncs.remove(prevFuncs.size() - 1);
            }
        } else if (nextFuncs.size() > 1) {
            progressed += nextFuncs.size() - 1;
            Collections.sort(nextFuncs, preferredTypeComparator);
            while (nextFuncs.size() > 1) {
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
            for (ResolvedFunction nextFunc : nextFuncs) {
                if (!directMatches.contains(nextFunc.getArgType())) {
                    logger.debug("removing next func: " + nextFunc);
                    nextFuncs.remove(nextFunc);
                    progressed++;
                }
            }
        }
        return progressed;
    }

    private Set<Class<?>> getOutputs(List<ResolvedFunction> prevFuncs) {
        Set<Class<?>> outputs = new HashSet<>();
        for (ResolvedFunction func : prevFuncs) {
            outputs.add(func.getReturnType());
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
    public List<String> getGeneratorNames() {
        List<String> genNames = new ArrayList<>();
        return new ArrayList<String>() {{
            add("compose");
        }};
    }
}
