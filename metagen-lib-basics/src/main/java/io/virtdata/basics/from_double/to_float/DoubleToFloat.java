package io.virtdata.basics.from_double.to_float;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.DoubleFunction;

@ThreadSafeMapper
public class DoubleToFloat implements DoubleFunction<Float> {
    @Override
    public Float apply(double value) {
        return (float) value;
    }
}
