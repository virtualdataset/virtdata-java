package io.virtdata.libimpl.continuous;

import io.virtdata.libimpl.ThreadSafeHash;

import java.util.function.DoubleUnaryOperator;
import java.util.function.LongToDoubleFunction;

public class RealLongDoubleSampler implements LongToDoubleFunction {

    private final DoubleUnaryOperator f;
    private ThreadSafeHash hash;

    public RealLongDoubleSampler(DoubleUnaryOperator parentFunc, boolean hash) {
        this.f = parentFunc;
        if (hash) {
            this.hash = new ThreadSafeHash();
        }
    }

    @Override
    public double applyAsDouble(long value) {
        if (hash!=null) {
            value = hash.applyAsLong(value);
        }
        double unit = (double) value / (double) Long.MAX_VALUE;
        double sample =f.applyAsDouble(unit);
        return sample;
    }
}
