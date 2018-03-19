package io.virtdata.conversions.from_long;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class ToFloat implements LongFunction<Float> {
    private final long scale;

    public ToFloat(long scale) {
        this.scale = scale;
    }
    public ToFloat() {
        this.scale = Long.MAX_VALUE;
    }

    @Override
    public Float apply(long input) {
        return (float)(input % scale);
    }
}
