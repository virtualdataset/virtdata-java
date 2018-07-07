package io.virtdata.conversions.from_long;

import java.util.function.LongFunction;

public class ToString implements LongFunction<String> {
    public String apply(long l) {
        return String.valueOf(l);
    }
}
