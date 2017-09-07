package io.virtdata.basics.from_long.to_long;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class Identity implements LongUnaryOperator {
    @Override
    public long applyAsLong(long operand) {
        return operand;
    }
}
