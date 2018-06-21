package io.virtdata.basicsmappers.stateful;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Save the current input value at this point in the function chain to a thread-local variable name.
 * The input value is unchanged, and available for the next function in the chain to use as-is.
 */
@ThreadSafeMapper
@Categories({Category.state})
public class Save implements Function<Object,Object> {

    private final String name;

    @Example({"Save('foo')","save the current input object value to the name 'foo' in this thread"})
    public Save(String name) {
        this.name = name;
    }

    @Override
    public Object apply(Object o) {

        HashMap<String, Object> map = ThreadLocalState.tl_ObjectMap.get();
        map.put(name,o);
        return o;
    }

}
