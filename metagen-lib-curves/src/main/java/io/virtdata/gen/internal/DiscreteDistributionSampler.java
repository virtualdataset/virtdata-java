package io.virtdata.gen.internal;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.function.LongUnaryOperator;
import java.util.stream.Collectors;

/**
 * This is *NOT* threadsafe. It will be reworked to provide a more functional integration with apache commons math, if possible.
 */
public class DiscreteDistributionSampler implements LongUnaryOperator {

    private String[] distributionDef;
    private HashedDiscreteSamplingAdapter samplingAdapter;

    // Odd constructors to work around commons reflection utils not recognizing varargs


    public DiscreteDistributionSampler(String distName) {
        this(new String[]{distName});
    }

    public DiscreteDistributionSampler(String distname, String param1) {
        this(new String[]{distname, param1});
    }

    public DiscreteDistributionSampler(String distname, String param1, String param2) {
        this(new String[]{distname, param1, param2});
    }

    public DiscreteDistributionSampler(String distname, String param1, String param2, String param3) {
        this(new String[]{distname, param1, param2, param3});
    }

    /**
     * Create a discrete distribution sampler.
     * @param distributionDef arguments for a discrete distribution, starting with the simple name
     */
    public DiscreteDistributionSampler(String[] distributionDef) {
        if (distributionDef.length < 1) {
            throw new InvalidParameterException("Double sampler requires at least a distribution name");
        }
        this.distributionDef = distributionDef;
        this.samplingAdapter = new HashedDiscreteSamplingAdapter(distributionDef);
    }


    public String toString() {
        return "distName:" + distributionDef[0] + ", params:" +
                Arrays.asList(distributionDef).stream().skip(1).collect(Collectors.joining(","));
    }

    @Override
    public long applyAsLong(long operand) {
        long sample = samplingAdapter.sample(operand);
        return sample;
    }
}
