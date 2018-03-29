package io.virtdata.basicsmappers.from_long.to_short;

import io.virtdata.annotations.DeprecatedFunction;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongFunction;

@ThreadSafeMapper
@DeprecatedFunction("This function is being replaced by ToShort() for naming consistency.")
public class LongToShort implements LongFunction<Short> {

    @Override
    public Short apply(long value) {
        return (short) (value & Short.MAX_VALUE);
    }
}
