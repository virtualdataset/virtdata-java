package io.virtdata.long_short;

import java.util.function.LongFunction;

public class ModuloToShort implements LongFunction<Short> {

    private final long modulo;

    public ModuloToShort(long modulo) {
        this.modulo = modulo;
    }

    @Override
    public Short apply(long value) {
        return (short) ((value % modulo) & Short.MAX_VALUE);
    }
}
