package io.virtdata.conversions.from_long;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class ToByte implements LongFunction<Byte> {

    private final int mod;
    public ToByte() {
        this.mod = Byte.MAX_VALUE;
    }
    public ToByte(int modulo) {
        this.mod = modulo;
    }

    @Override
    public Byte apply(long input) {
        return (byte)(input % mod);
    }
}
