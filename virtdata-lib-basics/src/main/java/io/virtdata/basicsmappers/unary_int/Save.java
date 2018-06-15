package io.virtdata.basicsmappers.unary_int;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.IntUnaryOperator;

@ThreadSafeMapper
public class Save implements IntUnaryOperator {

    private final String name;

    public Save(String name) {
        this.name = name;
    }

    @Override
    public int applyAsInt(int operand) {
        ThreadLocalState.tl_ObjectMap.get().put(name,operand);
        return operand;
    }
}
