package io.virtdata.functional;

import java.util.function.DoubleFunction;

public class DoubleToFloat implements DoubleFunction<Float> {
    @Override
    public Float apply(double value) {
        return (float) value;
    }
}
