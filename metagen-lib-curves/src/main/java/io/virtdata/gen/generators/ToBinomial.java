package io.virtdata.gen.generators;

import io.virtdata.gen.internal.RandomGeneratorAdapter;
import io.virtdata.api.Generator;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;

public class ToBinomial implements Generator<Long> {

    private final RandomGeneratorAdapter randomGeneratorAdapter;
    private final IntegerDistribution distribution;

    public ToBinomial(int trials, double p) {
        this.randomGeneratorAdapter = new RandomGeneratorAdapter();
        this.distribution = new BinomialDistribution(randomGeneratorAdapter,trials,p);
    }

    @Override
    public Long get(long input) {
        randomGeneratorAdapter.setSeed(input);
        distribution.reseedRandomGenerator(input);
        return null;
    }
}
