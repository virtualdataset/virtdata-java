package io.virtdata.gen.generators;

import io.virtdata.gen.internal.TLRDA;

import java.util.function.LongToDoubleFunction;

public class ToContinuousDistribution implements LongToDoubleFunction {

    private TLRDA adapter;

    public ToContinuousDistribution(String... args) {
        adapter = new TLRDA<>(args);
    }

    @Override
    public double applyAsDouble(long value) {
        return adapter.applyAsDouble(value);
    }
}
