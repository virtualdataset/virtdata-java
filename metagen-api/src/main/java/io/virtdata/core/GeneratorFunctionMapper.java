package io.virtdata.core;

import io.virtdata.api.Generator;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;

/**
 * <p>This class implements an obtuse way of avoiding autoboxing and M:N type
 * mapping complexity by way of doublish dispatch. It was preferred over a more
 * generalized reflection and annotation-based approach. If it gets too verbose,
 * (for some definition of "too"), then it may be refactored.</p>
 *
 * <p>The primary goal of this approach is to allow for primitive-level
 * lambdas when function are composed together. This will allow for significant
 * performance gains when there are only a few steps in a composed function
 * which are non-primitive, which is the general case.</p>
 *
 * <p>Composition should be supported between all primitive functions
 * for types listed in TypeMap, as well as generic functions, with generic
 * functions as the last resort.</p>
 */
@SuppressWarnings("unchecked")
public class GeneratorFunctionMapper {

    public  static <T> Generator<T> map(Object function) {
        if (function instanceof Generator) {
            return (Generator) function;
        }
        if (function instanceof LongUnaryOperator) {
            return (Generator<T>) map((LongUnaryOperator)function);
        }
        if (function instanceof LongFunction) {
            return (Generator<T>) map((LongFunction) function);
        }
        if (function instanceof Function) {
            return (Generator<T>) map ((Function)function);
        }
        if (function instanceof LongToIntFunction) {
            return (Generator<T>) map ((LongToIntFunction) function);
        }
        throw new RuntimeException(
                "Function object was not a recognized type for mapping to a generator lambda, object"
                        + function.toString());
    }

    public static Generator<Long> map(LongUnaryOperator f) {
        return f::applyAsLong;
    }

    public static <R> Generator<R> map(LongFunction<R> f) {
        return f::apply;
    }

    public static <R> Generator<R> map(Function<Long,R> f) {
        return f::apply;
    }

    public static Generator<Integer> map(LongToIntFunction f){
        return f::applyAsInt;
    }

}
