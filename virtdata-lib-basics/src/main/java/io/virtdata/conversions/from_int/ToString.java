package io.virtdata.conversions.from_int;

import java.util.function.IntFunction;

public class ToString implements IntFunction<String> {
    public String apply(int i) {
        return String.valueOf(i);
    }
}
