package io.virtdata.conversions.from_int;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.IntFunction;

@ThreadSafeMapper
public class ToShort implements IntFunction<Short> {

    private final int scale;

    public ToShort() {
        this.scale = Short.MAX_VALUE;
    }

    public ToShort(int scale) {
        this.scale = scale;
    }

    @Override
    public Short apply(int input) {
        return (short) (input % scale);
    }
}
