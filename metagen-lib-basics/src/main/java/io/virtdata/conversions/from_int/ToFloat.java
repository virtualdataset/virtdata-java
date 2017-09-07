package io.virtdata.conversions.from_int;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.IntFunction;

@ThreadSafeMapper
public class ToFloat implements IntFunction<Float> {
    private final int scale;

    public ToFloat(int scale) {
        this.scale = scale;
    }

    public ToFloat() {
        this.scale = Integer.MAX_VALUE;
    }

    @Override
    public Float apply(int input) {
        return (float) (input % scale);
    }
}
