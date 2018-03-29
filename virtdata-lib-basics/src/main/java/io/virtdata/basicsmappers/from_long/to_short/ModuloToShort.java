package io.virtdata.basicsmappers.from_long.to_short;

import io.virtdata.annotations.DeprecatedFunction;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongFunction;

@ThreadSafeMapper
@DeprecatedFunction("This function is being replaced by ToShort(modulo) for naming consistency.")
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
