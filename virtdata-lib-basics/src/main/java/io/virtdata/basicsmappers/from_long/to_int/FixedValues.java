package io.virtdata.basicsmappers.from_long.to_int;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class FixedValues implements LongToIntFunction {

    private final int[] fixedValues;

    public FixedValues(int... values) {
        this.fixedValues = values;
    }

    @Override
    public int applyAsInt(long value) {
        int index = (int)(value % Integer.MAX_VALUE) % fixedValues.length;
        return fixedValues[index];
    }
}