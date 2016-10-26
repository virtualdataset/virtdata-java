package io.virtdata.ints;

import java.util.function.IntUnaryOperator;

public class Mul implements IntUnaryOperator {

    public Mul(int addend) {
        this.addend = addend;
    }

    private int addend;

    @Override
    public int applyAsInt(int operand) {
        return operand * addend;
    }
}
