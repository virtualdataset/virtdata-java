package io.virtdata.basics.from_long.to_byte;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class LongToByte implements LongFunction<Byte> {
    @Override
    public Byte apply(long value) {
        return (byte) (value & Byte.MAX_VALUE);
    }
}
