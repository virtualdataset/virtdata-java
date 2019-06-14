package io.virtdata.libbasics.shared.unary_string;

import java.util.function.Function;

public class NullIfNullOrEmpty implements Function<String,String> {
    
    @Override
    public String apply(String s) {
        if (s==null || s.isEmpty()) {
            return null;
        }
        return s;
    }
}
