package io.virtdata.conversions.from_string;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToShort implements Function<String,Short> {

    @Override
    public Short apply(String input) {
        return Short.valueOf(input);
    }
}
