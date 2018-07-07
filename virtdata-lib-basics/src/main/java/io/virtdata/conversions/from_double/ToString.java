package io.virtdata.conversions.from_double;

import java.util.function.DoubleFunction;

public class ToString implements DoubleFunction<String> {

    public String apply(double v) {
        return String.valueOf(v);
    }
}
