package io.virtdata.conversions.from_long;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class ToShort implements LongFunction<Short> {

    private final int scale;

    public ToShort() {
        this.scale = Short.MAX_VALUE;
    }
    public ToShort(int wrapat) {
        this.scale = wrapat;
    }

    @Override
    public Short apply(long input) {
        return (short) (input % scale);
    }
}
