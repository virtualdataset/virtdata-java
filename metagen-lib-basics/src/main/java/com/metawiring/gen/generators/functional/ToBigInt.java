package com.metawiring.gen.generators.functional;

import com.metawiring.gen.metagenapi.Generator;

import java.math.BigInteger;

public class ToBigInt implements Generator<BigInteger> {
    @Override
    public BigInteger get(long input) {
        return BigInteger.valueOf(input);
    }
}
