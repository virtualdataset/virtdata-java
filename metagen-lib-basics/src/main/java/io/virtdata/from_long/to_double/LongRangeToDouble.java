package io.virtdata.from_long.to_double;

import io.virtdata.api.ThreadSafeMapper;

import java.util.function.LongToDoubleFunction;

@ThreadSafeMapper
public class LongRangeToDouble implements LongToDoubleFunction {

    @Override
    public double applyAsDouble(long value) {
        return (double) value;
    }
}
