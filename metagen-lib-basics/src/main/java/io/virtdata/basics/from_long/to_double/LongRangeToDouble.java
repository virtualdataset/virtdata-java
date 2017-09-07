package io.virtdata.basics.from_long.to_double;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.LongToDoubleFunction;

@ThreadSafeMapper
public class LongRangeToDouble implements LongToDoubleFunction {

    @Override
    public double applyAsDouble(long value) {
        return (double) value;
    }
}
