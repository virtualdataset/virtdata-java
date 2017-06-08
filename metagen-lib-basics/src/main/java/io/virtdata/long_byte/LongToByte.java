package io.virtdata.long_byte;

import java.util.function.LongFunction;

public class LongToByte implements LongFunction<Byte> {
    @Override
    public Byte apply(long value) {
        return (byte) (value & Byte.MAX_VALUE);
    }
}
