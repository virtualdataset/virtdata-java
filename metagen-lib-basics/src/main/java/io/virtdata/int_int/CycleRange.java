package io.virtdata.int_int;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.IntUnaryOperator;

@ThreadSafeMapper
public class CycleRange implements IntUnaryOperator {

    private final int minValue;
    private final int width;

    public CycleRange(int maxValue) {
        this(0,maxValue);
    }

    public CycleRange(int minValue, int maxValue) {
        this.minValue = minValue;

        if (maxValue<minValue) {
            throw new RuntimeException("CycleRange must have min and max value in that order.");
        }
        this.width = maxValue - minValue;
    }

    @Override
    public int applyAsInt(int operand) {
        return minValue + (operand % width);
    }
}
