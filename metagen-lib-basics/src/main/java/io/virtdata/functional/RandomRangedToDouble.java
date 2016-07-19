package io.virtdata.functional;

import io.virtdata.api.Generator;
import org.apache.commons.math3.random.MersenneTwister;

public class RandomRangedToDouble implements Generator<Double> {

    private MersenneTwister theTwister = new MersenneTwister(System.nanoTime());
    private long min;
    private long max;
    private long _length;

    public RandomRangedToDouble(long min, long max) {
        if (max<=min) {
            throw new RuntimeException("max must be >= min");
        }
        this.min = min;
        this.max = max;
        this._length = max - min;
    }

    @Override
    public Double get(long input) {
        Double value = Math.abs(theTwister.nextDouble());
        value %= _length;
        value += min;
        return value;
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + min + ":" + max;
    }

}

