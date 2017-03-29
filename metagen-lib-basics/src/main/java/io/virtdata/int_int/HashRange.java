package io.virtdata.int_int;

import java.util.function.IntUnaryOperator;

public class HashRange implements IntUnaryOperator {

    private final int minValue;
    private final int  width;
    private final Hash hash = new Hash();

    public HashRange(int minValue, int maxValue) {
        this.minValue = minValue;

        if (maxValue<minValue) {
            throw new RuntimeException("CycleRange must have min and max value in that order.");
        }
        this.width = maxValue - minValue;
    }

    @Override
    public int applyAsInt(int operand) {
        return minValue + (hash.applyAsInt(operand) & width);
    }
}
