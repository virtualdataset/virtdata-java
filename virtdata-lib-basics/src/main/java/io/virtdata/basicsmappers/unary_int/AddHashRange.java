package io.virtdata.basicsmappers.unary_int;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.IntUnaryOperator;

@ThreadSafeMapper
public class AddHashRange implements IntUnaryOperator {

    private final io.virtdata.basicsmappers.unary_int.HashRange hashRange;

    public AddHashRange(int maxValue) {
        this(0, maxValue);
    }

    public AddHashRange(int minValue, int maxValue) {
        this.hashRange = new io.virtdata.basicsmappers.unary_int.HashRange(minValue, maxValue);
    }

    @Override
    public int applyAsInt(int operand) {
        return operand + hashRange.applyAsInt(operand);
    }
}
