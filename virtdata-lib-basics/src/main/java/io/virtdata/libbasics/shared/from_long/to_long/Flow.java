package io.virtdata.libbasics.shared.from_long.to_long;

import io.virtdata.annotations.Example;

import java.util.function.LongUnaryOperator;

/**
 * Combine multiple unary operators into a single operator as
 * a higher-order function. This allows for flows to be used
 * in places where a single function is allowed.
 */
public class Flow implements LongUnaryOperator {

    private final LongUnaryOperator[] ops;

    @Example({"StringFlow(Add(3),Mul(6))","Create an integer operator which adds 3 and multiplies the result by 6"})
    public Flow(LongUnaryOperator... ops) {
        this.ops = ops;
    }

    @Override
    public long applyAsLong(long operand) {
        long value = operand;
        for (LongUnaryOperator op : ops) {
            value = op.applyAsLong(value);
        }
        return value;
    }
}
