package io.virtdata.long_double;

import java.util.function.LongToDoubleFunction;

public class LongRangeToDouble implements LongToDoubleFunction {

    @Override
    public double applyAsDouble(long value) {
        return (double) value;
    }
}
