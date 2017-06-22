package io.virtdata.long_long;

import java.util.function.LongUnaryOperator;

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
