package io.virtdata.conversions.from_double;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToBoolean implements Function<Double,Boolean> {

    @Override
    public Boolean apply(Double input) {
        return ((input.longValue()) & 1) == 1;
    }
}
