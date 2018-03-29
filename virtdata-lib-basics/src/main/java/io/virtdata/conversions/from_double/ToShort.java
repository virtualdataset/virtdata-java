package io.virtdata.conversions.from_double;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.DoubleFunction;

@ThreadSafeMapper
public class ToShort implements DoubleFunction<Short> {

    private final int scale;
    public ToShort() {
        this.scale = Short.MAX_VALUE;
    }
    public ToShort(int modulo) {
        this.scale = modulo;
    }

    @Override
    public Short apply(double input) {
        return (short)(((long) input) % scale);
    }
}
