package io.virtdata.ints;

import java.util.function.IntUnaryOperator;

public class AddInt implements IntUnaryOperator {

    private int addend;

    public AddInt(int addend) {
        this.addend = addend;
    }

    @Override
    public int applyAsInt(int operand) {
        return operand + addend;
    }
}
