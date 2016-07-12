package io.virtdata.functional;

import io.virtdata.api.Generator;

import java.math.BigInteger;

public class ToBigInt implements Generator<BigInteger> {
    @Override
    public BigInteger get(long input) {
        return BigInteger.valueOf(input);
    }
}
