package io.virtdata.libimpl.continuous;

import java.util.function.LongToDoubleFunction;

public class InterpolatedLongToDoubleFunction implements LongToDoubleFunction {
    private final LongToDoubleFunction f;
    private final int resolution;
    private double[] lut;

    public InterpolatedLongToDoubleFunction(int resolution, LongToDoubleFunction f) {
        this.f = f;
        this.resolution = resolution;
        precompute();
    }

    @Override
    public double applyAsDouble(long value) {
        double unitSample = ((double) value)/(double) Long.MAX_VALUE;
        int leftidx = (int) (unitSample * (double) resolution);
        double leftPartial = unitSample - leftidx;

        double leftComponent=(lut[leftidx] * (1.0-leftPartial));
        double rightComponent = (lut[leftidx+1] * leftPartial);

        double sample = leftComponent + rightComponent;
        return sample;
    }

    private void precompute() {
        for (int s = 0; s <= resolution; s++) { // not a ranging error
            double rangedToUnit = (double) s / (double) resolution;
            lut[s] = f.applyAsDouble((long)(rangedToUnit*Long.MAX_VALUE));
        }
        lut[lut.length-1]=0.0D; // only for right of max, when S==Max in the rare case
    }

}
