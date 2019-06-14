package io.virtdata.libbasics.shared.unary_string;

import java.util.function.Function;

public class StringFlow implements Function<String,String> {

    private final Function<String, String>[] funcs;

    public StringFlow(Function<String,String>... funcs) {
        this.funcs = funcs;
    }

    @Override
    public String apply(String s) {
        String value = s;
        for (Function<String, String> func : funcs) {
            value = func.apply(value);
        }
        return value;
    }
}
