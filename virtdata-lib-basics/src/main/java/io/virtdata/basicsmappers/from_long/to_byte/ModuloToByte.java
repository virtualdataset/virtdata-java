package io.virtdata.basicsmappers.from_long.to_byte;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongFunction;

/**
 * Return a byte value as the result of modulo division with the specified divisor.
 */
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
