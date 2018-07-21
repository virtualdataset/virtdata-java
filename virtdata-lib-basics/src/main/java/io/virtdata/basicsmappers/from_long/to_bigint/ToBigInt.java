package io.virtdata.basicsmappers.from_long.to_bigint;

import io.virtdata.annotations.ThreadSafeMapper;

import java.math.BigInteger;
import java.util.function.LongFunction;

/**
 * Convert the input value to a {@code BigInteger}
 */
@ThreadSafeMapper
public class ToBigInt implements LongFunction<BigInteger> {
    @Override
    public BigInteger apply(long input) {
        return BigInteger.valueOf(input);
    }
}
