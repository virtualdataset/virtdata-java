package io.virtdata.conversions.from_double;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.DoubleFunction;

@ThreadSafeMapper
public class ToFloat implements DoubleFunction<Float> {
    private final double scale;

    public ToFloat(double scale) {
        this.scale = scale;
    }
    public ToFloat() {
        this.scale = Float.MAX_VALUE;
    }

    @Override
    public Float apply(double input) {
        return (float) (input % scale);
    }
}
