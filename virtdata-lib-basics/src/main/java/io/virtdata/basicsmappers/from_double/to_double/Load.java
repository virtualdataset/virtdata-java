package io.virtdata.basicsmappers.from_double.to_double;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.DoubleUnaryOperator;

@ThreadSafeMapper
public class Load implements DoubleUnaryOperator {

    private final String name;

    @Example({"Load('foo')","load a double value from the named variable for this thread"})
    public Load(String name) {
        this.name = name;
    }

    @Override
    public double applyAsDouble(double operand) {
        Object o = ThreadLocalState.tl_ObjectMap.get().get(name);
        return (double) o;
    }
}
