package io.virtdata.strings;

import java.util.function.Function;

public class ToString implements Function<Object,String> {

    @Override
    public String apply(Object o) {
        return String.valueOf(o);
    }
}
