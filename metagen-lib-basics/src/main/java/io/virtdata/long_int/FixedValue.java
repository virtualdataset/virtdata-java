package io.virtdata.long_int;

import io.virtdata.api.ThreadSafeMapper;

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
