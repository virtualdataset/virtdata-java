package io.virtdata.basicsmappers.from_long.to_int;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class ToInt implements LongToIntFunction{
    @Override
    public int applyAsInt(long value) {
        return (int) (value % Integer.MAX_VALUE);
    }
}
