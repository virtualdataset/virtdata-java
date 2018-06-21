package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.LongUnaryOperator;

/**
 * Load the named value from the per-thread variable map. The current input value
 * is discarded and replaced with the value.
 */
@ThreadSafeMapper
public class Load implements LongUnaryOperator {

    private final String name;

    @Example({"Load('foo')","load a long value from the named variable for this thread"})
    public Load(String name) {
        this.name = name;
    }

    @Override
    public long applyAsLong(long operand) {
        Object o = ThreadLocalState.tl_ObjectMap.get().get(name);
        return (long) o;
    }
}
