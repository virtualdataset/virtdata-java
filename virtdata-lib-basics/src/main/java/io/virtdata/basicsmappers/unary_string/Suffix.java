package io.virtdata.basicsmappers.unary_string;

import io.virtdata.annotations.Description;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@Description("adds a String suffix to the input")
@ThreadSafeMapper
public class Suffix implements Function<String,String> {
    private String suffix;

    public Suffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String apply(String s) {
        return s + suffix;
    }
}
