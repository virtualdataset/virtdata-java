package io.virtdata.conversions.from_double;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.DoubleFunction;

@ThreadSafeMapper
public class ToByte implements DoubleFunction<Byte> {

    private final int scale;
    public ToByte() {
        this.scale = Byte.MAX_VALUE;
    }
    public ToByte(int modulo) {
        this.scale = modulo;
    }

    @Override
    public Byte apply(double input) {
        return (byte)(((long) input) % scale);
    }
}
