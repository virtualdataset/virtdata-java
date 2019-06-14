package io.virtdata.libbasics.shared.unary_string;

import java.util.function.Function;

public class NullIfEmpty implements Function<String,String> {

    @Override
    public String apply(String s) {
        if (s!=null && s.isEmpty()) {
            return null;
        }
        if (s!=null) {
            return s;
        }
        throw new RuntimeException("This function is not able to take null values as input. If you need to do that, consider using NullIfNullOrEmpty()");
    }
}
