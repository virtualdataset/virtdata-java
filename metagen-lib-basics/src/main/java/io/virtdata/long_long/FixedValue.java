package io.virtdata.long_long;

import java.util.function.LongUnaryOperator;

public class FixedValue implements LongUnaryOperator {

    private final long fixedValue;

    public FixedValue(long fixedValue) {
        this.fixedValue = fixedValue;
    }

    @Override
    public long applyAsLong(long operand) {
        return fixedValue;
    }
}
