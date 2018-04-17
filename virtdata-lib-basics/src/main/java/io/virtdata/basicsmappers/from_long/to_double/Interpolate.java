package io.virtdata.basicsmappers.from_long.to_double;

import io.virtdata.annotations.*;

import java.util.function.LongToDoubleFunction;

@Description("Return an interpolated number along a ")
@Input(Range.NonNegativeLongs)
@Output(Range.Doubles)
@Example("Interpolate(0.0,100.0) // return a linear value, 0L -> 0.0D, Long.MAX_VALUE -> 100.0D")
@ThreadSafeMapper
public class Interpolate implements LongToDoubleFunction {

    private final int resolution;
    private final double[] lut;

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
