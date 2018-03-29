package io.virtdata.basicsmappers.from_double.to_float;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.DoubleFunction;

@ThreadSafeMapper
public class DoubleToFloat implements DoubleFunction<Float> {
    @Override
    public Float apply(double value) {
        return (float) value;
    }
}
