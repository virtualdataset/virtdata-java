package io.virtdata.libbasics.shared.from_long.to_double;

import io.virtdata.annotations.*;

import java.util.function.LongToDoubleFunction;

@ThreadSafeMapper
public class Interpolate implements LongToDoubleFunction {

    private final int resolution;
    private final double[] lut;

    @Example({"Interpolate(0.0d,100.0d)","return a uniform double value between 0.0d and 100.0d"})
    @Example({"Interpolate(0.0d,90.0d,95.0d,98.0d,100.0d)","return a weighted double value where the first second and third quartiles are 90.0D, 95.0D, and 98.0D"})
    public Interpolate(double... value) {
        this(value.length, value);
    }

    public Interpolate(int resolution, double[] lut) {
        this.resolution = resolution;
        this.lut = lut;
    }

    @Override
    public double applyAsDouble(long input) {
        long value = input;
        double unit = (double) value / (double) Long.MAX_VALUE;
        double samplePoint = unit * resolution;
        int leftidx = (int) samplePoint;
        double leftPartial = samplePoint - leftidx;

        double leftComponent=(lut[leftidx] * (1.0-leftPartial));
        double rightComponent = (lut[leftidx+1] * leftPartial);

        double sample = leftComponent + rightComponent;
        return sample;
    }

}
