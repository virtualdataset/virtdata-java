package io.virtdata.conversions.from_int;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToBoolean implements Function<Integer,Boolean> {

    @Override
    public Boolean apply(Integer input) {
        return ((input & 1) == 1);
    }
}
