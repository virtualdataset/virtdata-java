package io.virtdata.basicsmappers.unary_string;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.Function;

@ThreadSafeMapper
public class Load implements Function<String,String> {

    private final String name;

    @Example({"Load('foo')","for the current thread, load a String value from the named variable"})
    public Load(String name) {
        this.name = name;
    }

    @Override
    public String apply(String s) {
        Object o = ThreadLocalState.tl_ObjectMap.get().get(name);
        return (String) o;
    }

}
