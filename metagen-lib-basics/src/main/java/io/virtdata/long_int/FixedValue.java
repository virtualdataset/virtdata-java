package io.virtdata.long_int;

import java.util.function.LongToIntFunction;

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
