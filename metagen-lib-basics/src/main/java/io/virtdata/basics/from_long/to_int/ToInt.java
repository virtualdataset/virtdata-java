package io.virtdata.basics.from_long.to_int;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class ToInt implements LongToIntFunction{
    @Override
    public int applyAsInt(long value) {
        return (int) (value % Integer.MAX_VALUE);
    }
}
