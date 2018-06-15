package io.virtdata.basicsmappers.unary_string;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.Function;

@ThreadSafeMapper
public class Save implements Function<String,String> {

    private final String name;

    public Save(String name) {
        this.name = name;
    }

    @Override
    public String apply(String s) {
        ThreadLocalState.tl_ObjectMap.get().put(name,s);
        return s;
    }

}
