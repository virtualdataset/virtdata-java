package io.virtdata.basicsmappers.unary_string;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.Function;

@ThreadSafeMapper
@Categories({Category.state})
public class Save implements Function<String,String> {

    private final String name;

    @Example({"Save('foo')","save the current String value to the name 'foo' in this thread"})
    public Save(String name) {
        this.name = name;
    }

    @Override
    public String apply(String s) {
        ThreadLocalState.tl_ObjectMap.get().put(name,s);
        return s;
    }

}
