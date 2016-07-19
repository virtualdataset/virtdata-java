package io.virtdata.mappers.discrete;

import java.util.function.LongUnaryOperator;

public class IDistTransform implements LongUnaryOperator {

    private IDistThreadLocal<?> adapter;

    public IDistTransform(String... args) {
        adapter = new IDistThreadLocal<>(args);
    }

    @Override
    public long applyAsLong(long operand) {
        long value = adapter.applyAsLong(operand);
        return value;
    }

}
