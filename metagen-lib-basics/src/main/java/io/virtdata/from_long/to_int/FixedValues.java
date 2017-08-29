package io.virtdata.from_long.to_int;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class FixedValues implements LongToIntFunction {

    private final int[] fixedValues;

    public FixedValues(int value) {
        this.fixedValues = new int[] { value };
    }
    public FixedValues(int value1, int value2) {
        this.fixedValues = new int[] { value1, value2 };
    }
    public FixedValues(int value1, int value2, int value3) {
        this.fixedValues = new int[] { value1, value2, value3 };
    }
    public FixedValues(int value1, int value2, int value3, int value4) {
        this.fixedValues = new int[] { value1, value2, value3, value4 };
    }
    public FixedValues(int value1, int value2, int value3, int value4, int value5) {
        this.fixedValues = new int[] { value1, value2, value3, value4, value5 };
    }

    @Override
    public int applyAsInt(long value) {
        int index = (int)(value % Integer.MAX_VALUE) % fixedValues.length;
        return fixedValues[index];
    }
}
