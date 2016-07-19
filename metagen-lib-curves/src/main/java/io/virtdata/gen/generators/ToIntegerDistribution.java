package io.virtdata.gen.generators;

import io.virtdata.gen.internal.TLIDA;

import java.util.function.LongUnaryOperator;

public class ToIntegerDistribution implements LongUnaryOperator {

    private TLIDA<?> adapter;

    public ToIntegerDistribution(String... args) {
        adapter = new TLIDA<>(args);
    }

    @Override
    public long applyAsLong(long operand) {
        long value = adapter.applyAsLong(operand);
        return value;
    }

}
