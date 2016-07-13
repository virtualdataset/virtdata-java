package io.virtdata.functional;

import java.math.BigInteger;
import java.util.function.LongFunction;

public class ToBigInt implements LongFunction<BigInteger> {
    @Override
    public BigInteger apply(long input) {
        return BigInteger.valueOf(input);
    }
}
