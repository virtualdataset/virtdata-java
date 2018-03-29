package io.virtdata.conversions.from_string;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToFloat implements Function<String,Float> {
    @Override
    public Float apply(String input) {
        return Float.valueOf(input);
    }
}
