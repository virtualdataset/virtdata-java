package io.virtdata.functional;

import io.virtdata.api.Desc;
import io.virtdata.api.Example;

import java.util.function.LongUnaryOperator;

/**
 * This is here for completeness. It's nothing but an identity function, only
 * named in a way that it can be found. It's better to use the non-deprecated
 * {@link io.virtdata.long_long.Identity} function.
 */
@Desc("an identify function that simply returns the input as long. Deprecated: use Identity() instead.")
@Example("CycleNumToLong")
@Deprecated
public class CycleNumToLong implements LongUnaryOperator {

    @Override
    public long applyAsLong(long operand) {
        return operand;
    }
}
