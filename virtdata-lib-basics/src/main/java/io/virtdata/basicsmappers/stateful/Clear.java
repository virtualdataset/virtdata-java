package io.virtdata.basicsmappers.stateful;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.HashMap;
import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
@Categories({Category.state})
public class Clear implements LongUnaryOperator {

    private final String[] names;

    public Clear() {
        this.names=null;
    }
    public Clear(String... names) {
        this.names = names;
    }

    @Override
    public long applyAsLong(long operand) {

        HashMap<String, Object> map = ThreadLocalState.tl_ObjectMap.get();
        if (names==null) {
            map.clear();
            return operand;
        }
        for (String name : names) {
            map.remove(name);
        }

        return operand;
    }
}
