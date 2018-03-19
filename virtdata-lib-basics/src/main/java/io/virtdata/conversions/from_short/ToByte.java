package io.virtdata.conversions.from_short;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToByte implements Function<Short,Byte> {

    private final int scale;

    public ToByte(int scale) {
        this.scale = scale;
    }
    public ToByte() {
        this.scale = Byte.MAX_VALUE;
    }

    @Override
    public Byte apply(Short input) {
        return (byte) (input.intValue() % scale);
    }
}
