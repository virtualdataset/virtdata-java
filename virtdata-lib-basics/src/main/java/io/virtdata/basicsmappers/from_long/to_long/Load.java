package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class Load implements LongUnaryOperator {

    private final String name;

    public Load(String name) {
        this.name = name;
    }

    @Override
    public long applyAsLong(long operand) {
        Object o = ThreadLocalState.tl_ObjectMap.get().get(name);
        return (long) o;
    }
}
