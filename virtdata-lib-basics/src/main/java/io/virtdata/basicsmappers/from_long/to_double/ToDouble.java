package io.virtdata.basicsmappers.from_long.to_double;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongToDoubleFunction;

/**
 * Convert the input value to a double.
 */
@ThreadSafeMapper
public class ToDouble implements LongToDoubleFunction {

    @Override
    public double applyAsDouble(long value) {
        return (double) value;
    }
}
