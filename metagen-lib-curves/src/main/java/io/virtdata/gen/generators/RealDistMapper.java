package io.virtdata.gen.generators;

import org.apache.commons.math3.distribution.RealDistribution;

import java.util.function.LongFunction;

public class RealDistMapper implements LongFunction<Double> {

    /**
     * WeibullDistribution(double alpha, double beta)
     * org.apache.commons.math3.distribution.WeibullDistribution WeibullDistribution(double, double)
     * org.apache.commons.math3.distribution.WeibullDistribution WeibullDistribution(double, double, double)
     * org.apache.commons.math3.distribution.WeibullDistribution WeibullDistribution(org.apache.commons.math3.random.RandomGenerator, double, double)
     * org.apache.commons.math3.distribution.WeibullDistribution WeibullDistribution(org.apache.commons.math3.random.RandomGenerator, double, double, double)
     *
     *
     */
    private RealDistribution realDistribution;

    public RealDistMapper(String distname, String... distargs) {

    }

    @Override
    public Double apply(long value) {
        return null;
    }
}
