package io.virtdata.core;

import io.virtdata.api.FunctionType;
import io.virtdata.api.Generator;

import java.util.function.*;

/**
 * <p>This class implements an obtuse way of avoiding autoboxing and M:N type
 * mapping complexity by way of doublish dispatch. It was preferred over a more
 * generalized reflection and annotation-based approach. If it gets too verbose,
 * (for some definition of "too"), then it may be refactored.</p>
 * <p>The primary goal of this approach is to allow for primitive-level
 * lambdas when function are composed together. This will allow for significant
 * performance gains when there are only a few steps in a composed function
 * which are non-primitive, which is the general case.</p>
 * <p>Composition should be supported between all primitive functions
 * for types listed in TypeMap, as well as generic functions, with generic
 * functions as the last resort.</p>
 */
@SuppressWarnings("unchecked")
public class GeneratorFunctionMapper {

    public static <T> Generator<T> map(Object function) {
        FunctionType functionType = FunctionType.valueOf(function);

        switch (functionType) {
            case long_double:
                return (Generator<T>) map((LongToDoubleFunction) function);
            case long_int:
                return (Generator<T>) map((LongToIntFunction) function);
            case long_long:
                return (Generator<T>) map((LongUnaryOperator) function);
            case long_T:
                return (Generator<T>) map((LongFunction) function);
            case R_T:
                return (Generator<T>) map((Function) function);
            case int_int:
                return (Generator<T>) map((IntUnaryOperator) function);
            case int_long:
                return (Generator<T>) map((IntToLongFunction) function);
            case int_double:
                return (Generator<T>) map((IntToDoubleFunction) function);
            case int_T:
                return (Generator<T>) map((IntFunction) function);
        }

        throw new RuntimeException(
                "Function object was not a recognized type for mapping to a generator lambda, object"
                        + function.toString());


    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static <R> Generator<R> map(IntFunction<R> f) {
        return (long l) -> f.apply((int) l);
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static Generator<Long> map(IntToDoubleFunction f) {
        return (long l) -> (long) f.applyAsDouble((int) l);
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static Generator<Long> map(IntToLongFunction f) {
        return (long l) -> f.applyAsLong((int) l);
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static Generator<Integer> map(IntUnaryOperator f) {
        return (long l) -> f.applyAsInt((int) l);
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static Generator<Double> map(LongToDoubleFunction f) {
        return f::applyAsDouble;
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static Generator<Integer> map(LongToIntFunction f) {
        return f::applyAsInt;
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static Generator<Long> map(LongUnaryOperator f) {
        return f::applyAsLong;
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static <R> Generator<R> map(LongFunction<R> f) {
        return f::apply;
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static <R> Generator<R> map(Function<Long, R> f) {
        return f::apply;
    }

}
