package io.virtdata.gen.internal;

import org.apache.commons.math3.distribution.IntegerDistribution;

/**
 * This is *NOT* threadsafe. It will be reworked to provide a more functional integration with apache commons math, if possible.
 */
public class HashedDiscreteSamplingAdapter {

    private RandomGeneratorAdapter randomMapper;
    private IntegerDistribution dist;


    public HashedDiscreteSamplingAdapter(int minValue, int maxValue, String distributionName, String... distributionParams) {
        randomMapper = new RandomGeneratorAdapter();
        Class<? extends IntegerDistribution> distClass = SizedDistributionMapper.mapIntegerDistributionClass(distributionName);
        dist = SizedDistributionMapper.mapIntegerDistribution(distClass, minValue, maxValue, randomMapper, distributionParams);
    }

    public HashedDiscreteSamplingAdapter(String[] distributionDef) {
        randomMapper = new RandomGeneratorAdapter();
        dist = SizedDistributionMapper.mapIntegerDistribution(randomMapper, distributionDef);
    }

    public long sample(long sampleId) {
        randomMapper.setSeed(sampleId);
        long sample=dist.sample();
        return sample;
    }

}
