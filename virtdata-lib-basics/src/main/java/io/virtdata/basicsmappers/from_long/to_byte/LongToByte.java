package io.virtdata.basicsmappers.from_long.to_byte;

import io.virtdata.annotations.DeprecatedFunction;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongFunction;

/**
 * Convert the input long value to a byte, with negative values
 * masked away.
 */
@ThreadSafeMapper
@DeprecatedFunction("This function is being replaced by ToByte() for naming consistency.")
public class LongToByte implements LongFunction<Byte> {
    @Override
    public Byte apply(long value) {
        return (byte) (value & Byte.MAX_VALUE);
    }
}
