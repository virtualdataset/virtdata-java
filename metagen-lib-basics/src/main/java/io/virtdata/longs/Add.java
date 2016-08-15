package io.virtdata.longs;

import java.util.function.LongUnaryOperator;

public class AddLong implements LongUnaryOperator {

    private final long addend;

    public AddLong(long addend) {
        this.addend = addend;
    }

    @Override
    public long applyAsLong(long operand) {
        return addend + operand;
    }
}
