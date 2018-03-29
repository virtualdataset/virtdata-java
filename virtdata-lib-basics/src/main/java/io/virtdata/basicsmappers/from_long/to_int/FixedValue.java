package io.virtdata.basicsmappers.from_long.to_int;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class FixedValue implements LongToIntFunction {

    private final int fixedValue;

    public FixedValue(int value) {
        this.fixedValue = value;
    }

    @Override
    public int applyAsInt(long value) {
        return fixedValue;
    }
}
