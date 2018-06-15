package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class Save implements LongUnaryOperator {

    private final String name;

    public Save(String name) {
        this.name = name;
    }

    @Override
    public long applyAsLong(long operand) {
        ThreadLocalState.tl_ObjectMap.get().put(name,operand);
        return operand;
    }
}
