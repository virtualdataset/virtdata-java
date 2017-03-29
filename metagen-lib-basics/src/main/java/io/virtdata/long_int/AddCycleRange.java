package io.virtdata.long_int;

import io.virtdata.long_long.CycleRange;

import java.util.function.LongToIntFunction;

public class AddCycleRange implements LongToIntFunction {

    private final io.virtdata.long_long.CycleRange cycleRange;

    public AddCycleRange(long maxValue) {
        this(0, maxValue);
    }

    public AddCycleRange(long minValue, long maxValue) {
        this.cycleRange = new CycleRange(minValue,maxValue);
    }

    @Override
    public int applyAsInt(long operand) {
        return (int) ((operand + cycleRange.applyAsLong(operand)) & Integer.MAX_VALUE);
    }
}
