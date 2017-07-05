package io.virtdata.long_int;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class CycleRange implements LongToIntFunction {

    private final long minValue;
    private final long width;

    public CycleRange(long maxValue) {
        this(0,maxValue);
    }

    public CycleRange(long minValue, long maxValue) {
        this.minValue = minValue;

        if (maxValue<minValue) {
            throw new RuntimeException("CycleRange must have min and max value in that order.");
        }
        this.width = maxValue - minValue;
    }

    @Override
    public int applyAsInt(long operand) {
        return (int) ((minValue + (operand % width)) & Integer.MAX_VALUE);
    }
}
