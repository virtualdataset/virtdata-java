package io.virtdata.ints;

import java.util.function.IntUnaryOperator;

public class Add implements IntUnaryOperator {

    private int addend;

    public Add(int addend) {
        this.addend = addend;
    }

    @Override
    public int applyAsInt(int operand) {
        return operand + addend;
    }
}
