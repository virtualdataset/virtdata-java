package io.virtdata.long_long;

import java.util.function.LongUnaryOperator;

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
