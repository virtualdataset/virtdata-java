package io.virtdata.longs;

import java.util.function.LongUnaryOperator;

public class Identity implements LongUnaryOperator {
    @Override
    public long applyAsLong(long operand) {
        return operand;
    }
}
