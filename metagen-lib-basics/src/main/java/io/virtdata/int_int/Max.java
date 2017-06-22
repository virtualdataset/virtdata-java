package io.virtdata.int_int;

import java.util.function.IntUnaryOperator;

public class Max implements IntUnaryOperator {

    private final int max;

    public Max(int max) {
        this.max = max;
    }

    @Override
    public int applyAsInt(int operand) {
        return Math.max(operand,max);
    }
}
