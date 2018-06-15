package io.virtdata.basicsmappers.from_double.to_double;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.DoubleUnaryOperator;

@ThreadSafeMapper
public class Save implements DoubleUnaryOperator {

    private final String name;

    public Save(String name) {
        this.name = name;
    }

    @Override
    public double applyAsDouble(double operand) {
        ThreadLocalState.tl_ObjectMap.get().put(name,(int) operand);
        return operand;
    }
}
