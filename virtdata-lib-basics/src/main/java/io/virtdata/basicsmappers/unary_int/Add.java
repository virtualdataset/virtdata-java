package io.virtdata.basicsmappers.unary_int;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.IntUnaryOperator;

@ThreadSafeMapper
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
