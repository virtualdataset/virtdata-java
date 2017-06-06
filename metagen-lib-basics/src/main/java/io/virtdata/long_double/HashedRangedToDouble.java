package io.virtdata.long_double;

import io.virtdata.api.DataMapper;
import io.virtdata.long_long.Hash;

public class HashedRangedToDouble implements DataMapper<Double> {

    private final long min;
    private final long max;
    private final double length;
    private final Hash hash;

    public HashedRangedToDouble(long min, long max) {
        this(min,max,System.nanoTime());
    }

    public HashedRangedToDouble(long min, long max, long seed) {
        this.hash = new Hash();
        if (max<=min) {
            throw new RuntimeException("max must be >= min");
        }
        this.min = min;
        this.max = max;
        this.length = (double) max - min;
    }

    @Override
    public Double get(long input) {
        long bitImage = hash.applyAsLong(input);
        double value = Math.abs(Double.longBitsToDouble(bitImage));
        value %= length;
        value += min;
        return value;
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + min + ":" + max;
    }

}

