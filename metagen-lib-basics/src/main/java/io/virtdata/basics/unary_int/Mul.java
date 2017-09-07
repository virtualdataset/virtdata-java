package io.virtdata.basics.unary_int;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.IntUnaryOperator;

@ThreadSafeMapper
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
