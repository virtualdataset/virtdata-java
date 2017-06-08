package io.virtdata.long_short;

import java.util.function.LongFunction;

public class LongToShort implements LongFunction<Short> {

    @Override
    public Short apply(long value) {
        return (short) (value & Short.MAX_VALUE);
    }
}
