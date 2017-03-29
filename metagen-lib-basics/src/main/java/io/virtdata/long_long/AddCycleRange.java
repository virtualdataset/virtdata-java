package io.virtdata.long_long;

import java.util.function.LongUnaryOperator;

public class AddCycleRange implements LongUnaryOperator {

    private final CycleRange cycleRange;

    public AddCycleRange(long maxValue) {
        this(0, maxValue);
    }

    public AddCycleRange(long minValue, long maxValue) {
        this.cycleRange = new CycleRange(minValue,maxValue);
    }

    @Override
    public long applyAsLong(long operand) {
        return operand + cycleRange.applyAsLong(operand);
    }
}
