package io.virtdata.conversions.from_int;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.IntFunction;

@ThreadSafeMapper
public class ToByte implements IntFunction<Byte> {

    private final int scale;
    public ToByte() {
        this.scale = Byte.MAX_VALUE;
    }
    public ToByte(int modulo) {
        this.scale = modulo;
    }

    @Override
    public Byte apply(int input) {
        return (byte)(input % scale);
    }
}
