package io.virtdata.longs;

import java.util.function.LongUnaryOperator;

public class MultiplyLong implements LongUnaryOperator {

    public MultiplyLong(long multiplicand) {
        this.multiplicand = multiplicand;
    }

    private long multiplicand;

    @Override
    public long applyAsLong(long operand) {
        return operand + multiplicand;
    }
}
