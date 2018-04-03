package io.virtdata.functionadapters;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.*;

/**
 * Adapts any {@link FunctionalInterface} type to a LongFunction,
 * for use with higher-order functions, when they require a
 * LongFunction as an argument.
 */
@ThreadSafeMapper
public class ToLongFunction implements LongFunction<Object> {

    private LongFunction<?> function;

    public ToLongFunction(LongUnaryOperator op) {
        this.function = op::applyAsLong;
    }
    public ToLongFunction(Function<Long,Long> op) {
        this.function = op::apply;
    }
    public ToLongFunction(LongToIntFunction op) {
        this.function = op::applyAsInt;
    }
    public ToLongFunction(LongToDoubleFunction op) {
        this.function = op::applyAsDouble;
    }
    public ToLongFunction(LongFunction<?> func) {
        this.function = func;
    }


    @Override
    public Object apply(long value) {
        return function.apply(value);
    }
}
