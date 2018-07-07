package io.virtdata.basicsmappers.unary_string;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

/**
 * Converts the input to the most obvious string representation with String.valueOf(...).
 */
@ThreadSafeMapper
public class ToString implements Function<Object,String> {

    @Override
    public String apply(Object o) {
        return String.valueOf(o);
    }
}
