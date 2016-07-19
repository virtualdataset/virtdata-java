package io.virtdata.strings;

import java.util.function.Function;

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
