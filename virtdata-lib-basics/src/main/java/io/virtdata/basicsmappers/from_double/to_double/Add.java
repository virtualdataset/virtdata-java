package io.virtdata.basicsmappers.from_double.to_double;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.DoubleUnaryOperator;

@ThreadSafeMapper
public class Add implements DoubleUnaryOperator {

    private final double addend;

    public Add(double addend) {
        this.addend = addend;
    }

    @Override
    public double applyAsDouble(double operand) {
        return addend + operand;
    }
}
