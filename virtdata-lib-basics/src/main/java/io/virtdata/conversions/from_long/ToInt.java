package io.virtdata.conversions.from_long;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class ToInt implements LongToIntFunction {

    private final int scale;

    public ToInt(int scale) {
        this.scale = scale;
    }

    public ToInt() {
        this.scale = Integer.MAX_VALUE;
    }

    @Override
    public int applyAsInt(long input) {
        return (int) (input % scale);
    }
}
