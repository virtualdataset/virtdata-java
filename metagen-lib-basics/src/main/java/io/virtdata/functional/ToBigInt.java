package io.virtdata.functional;

import io.virtdata.api.ThreadSafeMapper;

import java.math.BigInteger;
import java.util.function.LongFunction;

@ThreadSafeMapper
public class ToBigInt implements LongFunction<BigInteger> {
    @Override
    public BigInteger apply(long input) {
        return BigInteger.valueOf(input);
    }
}
