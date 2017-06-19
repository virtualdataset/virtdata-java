package io.virtdata.long_long;

import java.util.function.LongUnaryOperator;

public class FixedValues implements LongUnaryOperator {

    private final long[] fixedValues;

    public FixedValues(long value) {
        this.fixedValues = new long[] { value };
    }
    public FixedValues(long value1, long value2) {
        this.fixedValues = new long[] { value1, value2 };
    }
    public FixedValues(long value1, long value2, long value3) {
        this.fixedValues = new long[] { value1, value2, value3 };
    }
    public FixedValues(long value1, long value2, long value3, long value4) {
        this.fixedValues = new long[] { value1, value2, value3, value4 };
    }
    public FixedValues(long value1, long value2, long value3, long value4, long value5) {
        this.fixedValues = new long[] { value1, value2, value3, value4, value5 };
    }

    @Override
    public long applyAsLong(long value) {
        int index = (int)(value % Integer.MAX_VALUE) % fixedValues.length;
        return fixedValues[index];
    }
}
