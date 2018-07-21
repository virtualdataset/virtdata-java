package io.virtdata.basicsmappers.unary_int;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.IntUnaryOperator;

@ThreadSafeMapper
@Categories({Category.state})
public class Save implements IntUnaryOperator {

    private final String name;

    @Example({"Save('foo')","save the current int value to the name 'foo' in this thread"})
    public Save(String name) {
        this.name = name;
    }

    @Override
    public int applyAsInt(int operand) {
        ThreadLocalState.tl_ObjectMap.get().put(name,operand);
        return operand;
    }
}
