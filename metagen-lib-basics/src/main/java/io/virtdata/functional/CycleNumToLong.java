package io.virtdata.functional;

import java.util.function.LongUnaryOperator;

/**
 * This is here for completeness. It's nothing but an identity function, only
 * named in a way that it can be found.
 */
public class CycleNumToLong implements LongUnaryOperator {

    @Override
    public long applyAsLong(long operand) {
        return operand;
    }
}
