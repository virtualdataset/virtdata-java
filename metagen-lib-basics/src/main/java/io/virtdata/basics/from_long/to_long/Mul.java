package io.virtdata.basics.from_long.to_long;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
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
