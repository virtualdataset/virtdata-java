package io.virtdata.basics.from_long.to_byte;

import io.basics.virtdata.api.ThreadSafeMapper;

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
