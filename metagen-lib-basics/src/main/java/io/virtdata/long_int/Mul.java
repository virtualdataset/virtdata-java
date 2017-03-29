package io.virtdata.long_int;

import java.util.function.LongToIntFunction;

public class Mul implements LongToIntFunction {

    public Mul(long multiplicand) {
        this.multiplicand = multiplicand;
    }

    private long multiplicand;

    @Override
    public int applyAsInt(long operand) {
        return (int) ((operand * multiplicand) % Integer.MAX_VALUE);
    }
}
