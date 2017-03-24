package io.virtdata.longs;

import java.util.function.LongUnaryOperator;

public class AddHashRange implements LongUnaryOperator {

    private final HashRange hashRange;

    public AddHashRange(long maxValue) {
        this(0, maxValue);
    }

    public AddHashRange(long minValue, long maxValue) {
        this.hashRange = new HashRange(minValue, maxValue);
    }

    @Override
    public long applyAsLong(long operand) {
        return operand + hashRange.applyAsLong(operand);
    }
}
