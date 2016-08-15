package io.virtdata.strings;

import io.virtdata.api.types.FDoc;
import io.virtdata.api.types.Input;
import io.virtdata.api.types.Output;

import java.util.function.Function;

@Input(String.class)
@Output(String.class)
@FDoc("adds a String suffix to the input")
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
