package io.virtdata.strings;

import io.virtdata.api.Desc;
import io.virtdata.api.ThreadSafeMapper;

import java.util.function.Function;

@Desc("adds a String suffix to the input")
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
