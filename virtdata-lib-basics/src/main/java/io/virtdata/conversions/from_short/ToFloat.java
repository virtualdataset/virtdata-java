package io.virtdata.conversions.from_short;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToFloat implements Function<Short,Float> {

    @Override
    public Float apply(Short input) {
        return (float) input.intValue();
    }
}
