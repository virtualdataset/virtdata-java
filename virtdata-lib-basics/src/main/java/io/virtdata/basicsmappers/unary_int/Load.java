package io.virtdata.basicsmappers.unary_int;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.IntUnaryOperator;

@ThreadSafeMapper
public class Load implements IntUnaryOperator {

    private final String name;

    public Load(String name) {
        this.name = name;
    }

    @Override
    public int applyAsInt(int operand) {
        Object o = ThreadLocalState.tl_ObjectMap.get().get(name);
        return (int) o;
    }

}
