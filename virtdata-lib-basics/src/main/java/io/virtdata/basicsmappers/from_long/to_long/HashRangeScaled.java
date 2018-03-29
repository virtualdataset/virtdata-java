package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

/**
 * Uses the input value as well to establish the upper bound of the
 * value produced from the hash.
 */
@ThreadSafeMapper
public class HashRangeScaled implements LongUnaryOperator {

    private Hash hash = new Hash();
    public HashRangeScaled() {
    }

    @Override
    public long applyAsLong(long operand) {
        if (operand==0) { return 0; }
        long hashed = hash.applyAsLong(operand);
        return hashed % operand;
    }
}
