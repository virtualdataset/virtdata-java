package io.virtdata.conversions.from_double;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.DoubleToIntFunction;

@ThreadSafeMapper
public class ToInt implements DoubleToIntFunction {

    private final int scale;

    public ToInt(int scale) {
        this.scale = scale;
    }

    public ToInt() {
        this.scale = Integer.MAX_VALUE;
    }

    @Override
    public int applyAsInt(double input) {
        return (int) (input % scale);
    }
}
