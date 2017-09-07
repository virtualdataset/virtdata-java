package io.virtdata.conversions.from_float;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToBoolean implements Function<Float,Boolean> {

    @Override
    public Boolean apply(Float input) {
        return ((input.longValue()) & 1) == 1;
    }
}
