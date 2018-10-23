package io.virtdata.basicsmappers.unary_string;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

/**
 * Trim the input value and return the result.
 */
@ThreadSafeMapper
public class Trim implements Function<String, String>{

    @Example({"Trim('HashedLineToString('foo')')", "Trims the input value of surrounding whitespace"})
    public Trim(){
    }

    @Override
    public String apply(String s) {
        return s.trim();
    }
}
