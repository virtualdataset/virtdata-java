package io.virtdata.long_long;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class Max implements LongUnaryOperator {

    private final long max;

    public Max(long max) {
        this.max = max;
    }

    @Override
    public long applyAsLong(long operand) {
        return Math.max(operand,max);
    }
}
