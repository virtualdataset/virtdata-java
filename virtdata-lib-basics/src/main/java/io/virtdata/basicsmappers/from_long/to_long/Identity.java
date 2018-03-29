package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class Identity implements LongUnaryOperator {
    @Override
    public long applyAsLong(long operand) {
        return operand;
    }
}
