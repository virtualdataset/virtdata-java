package io.virtdata.long_int;

import io.virtdata.long_long.Hash;

import java.util.function.LongToIntFunction;

public class HashRange implements LongToIntFunction {

    private final long minValue;
    private final long width;
    private final io.virtdata.long_long.Hash hash = new Hash();

    public HashRange(long minValue, long maxValue) {
        this.minValue = minValue;

        if (maxValue<minValue) {
            throw new RuntimeException("CycleRange must have min and max value in that order.");
        }
        this.width = maxValue - minValue;
    }

    @Override
    public int applyAsInt(long operand) {
        return (int) ((minValue + (hash.applyAsLong(operand) % width)) & Integer.MAX_VALUE);
    }
}
