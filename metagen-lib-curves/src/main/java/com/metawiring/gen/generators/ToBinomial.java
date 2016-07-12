package com.metawiring.gen.generators;

import com.metawiring.gen.internal.RandomGeneratorAdapter;
import com.metawiring.gen.metagenapi.Generator;
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
