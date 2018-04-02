package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.Description;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
@Description("Yields the maximum of either the input value or the 'max' parameter.")
public class Max implements LongUnaryOperator {

    private final long max;

    public Max(long max) {
        this.max = max;
    }

    @Override
    public long applyAsLong(long operand) {
        return Math.max(operand,max);
    }
}
