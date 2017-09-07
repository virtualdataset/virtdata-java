package io.virtdata.basics.from_long.to_double;

import io.basics.virtdata.api.ThreadSafeMapper;
import io.virtdata.basics.from_long.to_long.Hash;

import java.util.function.LongToDoubleFunction;

/**
 * <p>This simulates a uniform sample from a range of double values
 * via long hashing. This function attempts to take a double
 * unit interval value from a long/long division over the whole
 * range of long values but via double value types, thus providing
 * a very linear sample. This means that the range of double
 * values to be accessed will not fall along all possible doubles,
 * but will still provide suitable values for ranges close to
 * high-precision points in the IEEE floating point number line.
 * This suffices for most reasonable ranges in practice outside
 * of scientific computing, where large exponents put adjacent
 * IEEE floating point values much further apart.</p>
 *
 * <p>This should be consider the default double range sampling
 * function for most uses, when the exponent is not needed for
 * readability.</p>
 */
@ThreadSafeMapper
public class HashedDoubleRange implements LongToDoubleFunction {

    private final double min;
    private final double max;
    private final double interval;
    private final static double MAX_DOUBLE_VIA_LONG = (double) Long.MAX_VALUE;
    private final Hash hash = new Hash();

    public HashedDoubleRange(double min, double max) {
        this.min = min;
        this.max = max;
        this.interval = max - min;
    }

    @Override
    public double applyAsDouble(long value) {
        long hashed = hash.applyAsLong(value);
        double unitScale = ((double) hashed) / MAX_DOUBLE_VIA_LONG;
        double valueScaled =interval*unitScale + min;
        return valueScaled;
    }
}
