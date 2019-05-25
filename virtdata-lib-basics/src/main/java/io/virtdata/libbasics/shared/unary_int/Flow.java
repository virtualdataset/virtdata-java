package io.virtdata.libbasics.shared.unary_int;

import java.util.function.IntUnaryOperator;

public class Flow implements IntUnaryOperator {

    private final IntUnaryOperator[] ops;

    public Flow(IntUnaryOperator... ops) {
        this.ops = ops;
    }

    @Override
    public int applyAsInt(int operand) {
        int value = operand;
        for (IntUnaryOperator op : ops) {
            value = op.applyAsInt(value);
        }
        return value;
    }
}
