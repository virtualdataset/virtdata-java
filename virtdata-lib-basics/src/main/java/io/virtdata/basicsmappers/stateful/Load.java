package io.virtdata.basicsmappers.stateful;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Load a named value from the per-thread state map.
 * The previous input value will be forgotten, and the named value will replace it
 * before the next function in the chain.
 */
@ThreadSafeMapper
@Categories({Category.state})
public class Load implements Function<Object,Object> {

    private final String name;

    @Example({"Load('foo')","load a Object value from the named variable for this thread"})
    public Load(String name) {
        this.name = name;
    }

    @Override
    public Object apply(Object o) {

        HashMap<String, Object> map = ThreadLocalState.tl_ObjectMap.get();
        return map.get(name);
    }

}
