package io.virtdata.basicsmappers.unary_int;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.IntUnaryOperator;

/**
 * Scale the input to the
 */
@ThreadSafeMapper
public class Scale implements IntUnaryOperator {

    private final double scaleFactor;

    public Scale(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    @Override
    public int applyAsInt(int operand) {
        return (int) (scaleFactor * (double) operand);
    }
}
