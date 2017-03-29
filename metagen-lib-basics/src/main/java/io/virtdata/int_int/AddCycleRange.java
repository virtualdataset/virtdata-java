package io.virtdata.int_int;

import java.util.function.IntUnaryOperator;

public class AddCycleRange implements IntUnaryOperator {

    private final CycleRange cycleRange;

    public AddCycleRange(int maxValue) {
        this(0, maxValue);
    }

    public AddCycleRange(int minValue, int maxValue) {
        this.cycleRange = new CycleRange(minValue,maxValue);
    }

    @Override
    public int applyAsInt(int operand) {
        return operand + cycleRange.applyAsInt(operand);
    }
}
