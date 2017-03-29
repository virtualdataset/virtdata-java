package io.virtdata.long_int;

import java.util.function.LongToIntFunction;

public class Add implements LongToIntFunction {

    private final long addend;

    public Add(long addend) {
        this.addend = addend;
    }

    @Override
    public int applyAsInt(long value) {
        return (int) ((value + addend) % Integer.MAX_VALUE);
    }
}
