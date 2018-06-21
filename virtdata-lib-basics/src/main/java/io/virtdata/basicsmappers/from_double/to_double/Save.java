package io.virtdata.basicsmappers.from_double.to_double;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.DoubleUnaryOperator;

@ThreadSafeMapper
public class Save implements DoubleUnaryOperator {

    private final String name;

    @Example({"Save('foo')","save the current double value to the name 'foo' in this thread"})
    public Save(String name) {
        this.name = name;
    }

    @Override
    public double applyAsDouble(double operand) {
        ThreadLocalState.tl_ObjectMap.get().put(name,(int) operand);
        return operand;
    }
}
