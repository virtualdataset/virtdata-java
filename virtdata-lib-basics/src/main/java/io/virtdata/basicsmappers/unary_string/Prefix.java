package io.virtdata.basicsmappers.unary_string;

import io.virtdata.annotations.Description;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

/**
 * Created by sebastianestevez on 8/28/17.
 */

@Description("adds a String prefix to the input")
@ThreadSafeMapper
public class Prefix implements Function<String,String>{
    private String prefix;

    public Prefix(String prefix){
        this.prefix = prefix;
    }


    @Override
    public String apply(String s) {
        return prefix + s;
    }
}
