package io.virtdata.long_long;

import java.util.function.LongUnaryOperator;

public class Mul implements LongUnaryOperator {

    public Mul(long multiplicand) {
        this.multiplicand = multiplicand;
    }

    private long multiplicand;

    @Override
    public long applyAsLong(long operand) {
        return operand * multiplicand;
    }
}
