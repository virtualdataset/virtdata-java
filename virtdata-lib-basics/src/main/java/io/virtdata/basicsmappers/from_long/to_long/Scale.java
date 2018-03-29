package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

/**
 * Scale the input to the
 */
@ThreadSafeMapper
public class Scale implements LongUnaryOperator {

    private final double scaleFactor;

    public Scale(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    @Override
    public long applyAsLong(long value) {
        return (long) (scaleFactor * (double) value);
    }
}
