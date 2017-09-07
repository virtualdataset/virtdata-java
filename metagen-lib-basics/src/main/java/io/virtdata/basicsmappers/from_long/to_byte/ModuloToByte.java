package io.virtdata.basicsmappers.from_long.to_byte;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class ModuloToByte implements LongFunction<Byte> {

    private final long modulo;

    public ModuloToByte(long modulo) {
        this.modulo = modulo;
    }

    @Override
    public Byte apply(long value) {
        return (byte) ((value % modulo) % Byte.MAX_VALUE);
    }
}
