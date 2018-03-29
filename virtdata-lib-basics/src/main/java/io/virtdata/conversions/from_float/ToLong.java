package io.virtdata.conversions.from_float;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToLong implements Function<Float,Long> {

    private final long scale;

    public ToLong(long scale) {
        this.scale = scale;
    }

    public ToLong() {
        this.scale = Long.MAX_VALUE;
    }

    @Override
    public Long apply(Float input) {
        return input.longValue() % scale;
    }
}
