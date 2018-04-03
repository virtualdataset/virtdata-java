package io.virtdata.functionadapters;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class ToLongUnaryOperator implements LongUnaryOperator {

    private LongUnaryOperator operator;

    public ToLongUnaryOperator(LongFunction<Long> f) {
        this.operator = f::apply;
    }

    public ToLongUnaryOperator(Function<Long,Long> f) {
        this.operator = f::apply;
    }

    public ToLongUnaryOperator(LongUnaryOperator f) {
        this.operator = f;
    }

    @Override
    public long applyAsLong(long operand) {
        return operator.applyAsLong(operand);
    }
}
