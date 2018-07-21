package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.*;

import java.util.function.LongUnaryOperator;

/**
 * Return a value along an interpolation curve. This allows you to sketch a basic
 * density curve and describe it simply with just a few values. The number of values
 * provided determines the resolution of the internal lookup table that is used for
 * interpolation. The first value is always the 0.0 anchoring point on the unit interval.
 * The last value is always the 1.0 anchoring point on the unit interval. This means
 * that in order to subdivide the density curve in an interesting way, you need to provide
 * a few more values in between them. Providing two values simply provides a uniform
 * sample between a minimum and maximum value.
 *
 * The input range of this function is, as many of the other functions in this library,
 * based on the valid range of positive long values, between 0L and Long.MAX_VALUE inclusive.
 * This means that if you want to combine interpolation on this curve with the effect of
 * pseudo-random sampling, you need to put a hash function ahead of it in the flow.
 */
@ThreadSafeMapper
public class Interpolate implements LongUnaryOperator {

    private final int resolution;
    private final double[] lut;

    @Example({"Interpolate(0.0d,100.0d)","return a uniform long value between 0L and 100L"})
    @Example({"Interpolate(0.0d,90.0d,95.0d,98.0d,100.0d)","return a weighted long value where the first second and third quartiles are 90.0D, 95.0D, and 98.0D"})
    public Interpolate(double... value) {
        this(value.length, value);
    }

    public Interpolate(long... value) {
        double[] doubles = new double[value.length];
        for (int i = 0; i < doubles.length; i++) {
            doubles[i]=(double)value[i];
        }
        this.resolution=doubles.length;
        this.lut = doubles;
    }

    public Interpolate(int resolution, double[] lut) {
        this.resolution = resolution;
        this.lut = lut;
    }

    @Override
    public long applyAsLong(long input) {
        long value = input;
        double unit = (double) value / (double) Long.MAX_VALUE;
        double samplePoint = unit * resolution;
        int leftidx = (int) samplePoint;
        double leftPartial = samplePoint - leftidx;

        double leftComponent=(lut[leftidx] * (1.0-leftPartial));
        double rightComponent = (lut[leftidx+1] * leftPartial);

        double sample = leftComponent + rightComponent;
        return (long) sample;
    }
}
