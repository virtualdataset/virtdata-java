package io.virtdata.long_int;

import java.util.function.LongToIntFunction;

public class ToInt implements LongToIntFunction{
    @Override
    public int applyAsInt(long value) {
        return (int) (value % Integer.MAX_VALUE);
    }
}
