package io.virtdata.mappers.continuous;

import java.util.function.LongToDoubleFunction;

public class CDistTransform implements LongToDoubleFunction {

    private CDistThreadLocal adapter;

    public CDistTransform(String... args) {
        adapter = new CDistThreadLocal<>(args);
    }

    @Override
    public double applyAsDouble(long value) {
        return adapter.applyAsDouble(value);
    }
}
