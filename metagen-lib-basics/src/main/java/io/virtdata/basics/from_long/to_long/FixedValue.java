package io.virtdata.basics.from_long.to_long;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
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
