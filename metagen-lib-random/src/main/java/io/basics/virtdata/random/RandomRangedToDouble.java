package io.basics.virtdata.random;

import io.basics.virtdata.api.DataMapper;
import io.basics.virtdata.api.DeprecatedFunction;
import org.apache.commons.math3.random.MersenneTwister;

@DeprecatedFunction("random mappers are not deterministic. They will be replaced with hash-based functions.")
public class RandomRangedToDouble implements DataMapper<Double> {

    private final MersenneTwister theTwister;
    private final long min;
    private final long max;
    private final long length;

    public RandomRangedToDouble(long min, long max) {
        this(min,max,System.nanoTime());
    }

    public RandomRangedToDouble(long min, long max, long seed) {
        this.theTwister = new MersenneTwister(seed);
        if (max<=min) {
            throw new RuntimeException("max must be >= min");
        }
        this.min = min;
        this.max = max;
        this.length = max - min;
    }

    @Override
    public Double get(long input) {
        Double value = Math.abs(theTwister.nextDouble());
        value %= length;
        value += min;
        return value;
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + min + ":" + max;
    }

}

