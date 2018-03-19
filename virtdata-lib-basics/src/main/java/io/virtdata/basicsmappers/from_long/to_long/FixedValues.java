package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class FixedValues implements LongUnaryOperator {

    private final long[] fixedValues;

    public FixedValues(long... values) {
        this.fixedValues = values;
    }

    @Override
    public long applyAsLong(long value) {
        int index = (int)(value % Integer.MAX_VALUE) % fixedValues.length;
        return fixedValues[index];
    }
}
