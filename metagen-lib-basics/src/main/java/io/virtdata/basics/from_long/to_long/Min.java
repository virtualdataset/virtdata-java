package io.virtdata.basics.from_long.to_long;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class Min implements LongUnaryOperator {

    private final long min;

    public Min(long min) {
        this.min = min;
    }

    @Override
    public long applyAsLong(long operand) {
        return Math.min(operand, min);
    }
}
