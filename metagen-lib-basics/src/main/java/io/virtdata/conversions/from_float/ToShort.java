package io.virtdata.conversions.from_float;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToShort implements Function<Float,Short> {

    private final int scale;
    public ToShort() {
        this.scale = Short.MAX_VALUE;
    }
    public ToShort(int modulo) {
        this.scale = modulo;
    }

    @Override
    public Short apply(Float input) {
        return (short)((input.longValue()) % scale);
    }
}
