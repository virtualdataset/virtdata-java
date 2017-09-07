package io.virtdata.basicsmappers.from_long.to_int;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.basicsmappers.from_long.to_long.Hash;

import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class HashRange implements LongToIntFunction {

    private final long minValue;
    private final long width;
    private final Hash hash = new Hash();

    public HashRange(long fixedValue) {
        this.minValue=fixedValue;
        this.width=1;
    }

    public HashRange(long minValue, long maxValue) {
        this.minValue = minValue;

        if (maxValue<=minValue) {
            throw new RuntimeException("CycleRange must have min and max value in that order.");
        }
        this.width = maxValue - minValue;
    }

    @Override
    public int applyAsInt(long operand) {
        return (int) ((minValue + (hash.applyAsLong(operand) % width)) & Integer.MAX_VALUE);
    }
}
