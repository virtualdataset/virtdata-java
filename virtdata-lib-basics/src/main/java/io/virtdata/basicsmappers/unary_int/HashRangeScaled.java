package io.virtdata.basicsmappers.unary_int;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.IntUnaryOperator;

/**
 * Uses the input value as well to establish the upper bound of the
 * value produced from the hash.
 */
@ThreadSafeMapper
public class HashRangeScaled implements IntUnaryOperator {

    private final Hash hash = new Hash();

    @Override
    public int applyAsInt(int operand) {
        if (operand==0) { return 0; }
        return hash.applyAsInt(operand) % operand;
    }
}
