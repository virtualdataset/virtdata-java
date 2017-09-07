package io.virtdata.basics.from_long.to_int;

import io.basics.virtdata.api.ThreadSafeMapper;
import io.virtdata.basics.from_long.to_long.HashRange;

import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class AddHashRange implements LongToIntFunction {

    private final HashRange hashRange;

    public AddHashRange(long maxValue) {
        this(0, maxValue);
    }

    public AddHashRange(long minValue, long maxValue) {
        this.hashRange = new HashRange(minValue, maxValue);
    }

    @Override
    public int applyAsInt(long operand) {
        return (int) ((operand + hashRange.applyAsLong(operand)) & Integer.MAX_VALUE);
    }
}
