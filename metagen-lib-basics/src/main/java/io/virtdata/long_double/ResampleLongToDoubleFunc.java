package io.virtdata.long_double;

import io.virtdata.api.DataMapper;
import io.virtdata.core.AllDataMapperLibraries;

import java.util.Optional;
import java.util.function.LongToDoubleFunction;

public class ResampleLongToDoubleFunc implements LongToDoubleFunction {

    private final String specifier;
    private final int resolution;
    private final double[] sampleData;
    private final double min;
    private final double max;
    private double undefinedValue = Double.NEGATIVE_INFINITY;
    private final DataMapper<Double> doubleMapper;

    public ResampleLongToDoubleFunc(String specifier, double min, double max, int resolution) {
        this.specifier = specifier;
        this.min = min;
        this.max = max;
        this.resolution = resolution;

        this.sampleData = new double[resolution+2]; // +1 for bracketing, +1 for ranging

        Optional<DataMapper<Double>> dataMapperOptional = AllDataMapperLibraries.get().getDoubleDataMapper(specifier);
        doubleMapper = dataMapperOptional.orElseThrow(
                () -> new RuntimeException("Unable to map specifier: " + specifier)
        );
        preComputeSampleValues();
    }

    private void preComputeSampleValues() {
        for (int i = 0; i <= resolution; i++) {
            double unitX = (double) i / (double) resolution;
            double scaled = min + ((max-min)*unitX);
            sampleData[i]= doubleMapper.get((long)scaled);
        }
        sampleData[resolution+1]=0.0;
    }

    @Override
    public double applyAsDouble(long value) {
        double rangedToLongUnit = (double) value / (double) Long.MAX_VALUE;
        double rangedToSampleGamut = rangedToLongUnit * (resolution);
        int leftIdx = (int) (rangedToSampleGamut);
        double fractionalLeft = (rangedToSampleGamut) - leftIdx;
        double left = sampleData[leftIdx];
        double right = sampleData[leftIdx+1];

        double linearInterpVal = (left * (1.0-fractionalLeft)) + (right * fractionalLeft);
        return linearInterpVal;
    }
}
