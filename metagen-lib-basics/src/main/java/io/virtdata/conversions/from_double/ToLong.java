package io.virtdata.conversions.from_double;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.DoubleToLongFunction;

@ThreadSafeMapper
public class ToLong implements DoubleToLongFunction {

    private final long scale;

    public ToLong(long scale) {
        this.scale = scale;
    }

    public ToLong() {
        this.scale = Long.MAX_VALUE;
    }

    @Override
    public long applyAsLong(double input) {
        return (long) (input % scale);
    }
}
