package io.virtdata.conversions.from_float;

import java.util.function.Function;

public class ToString implements Function<Float,String> {

    public String apply(Float aFloat) {
        return String.valueOf(aFloat.floatValue());
    }
}
