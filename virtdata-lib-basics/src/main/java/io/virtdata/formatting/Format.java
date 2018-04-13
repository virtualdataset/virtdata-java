package io.virtdata.formatting;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class Format implements Function<Object,String> {

    private final String format;

    public Format(String format) {
        this.format = format;
    }

    @Override
    public String apply(Object o) {
        return String.format(format,o);
    }
}
