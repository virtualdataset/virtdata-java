package io.virtdata.conversions.from_string;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToInt implements Function<String,Integer> {

    @Override
    public Integer apply(String input) {
        return Integer.valueOf(input);
    }
}
