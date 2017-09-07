package io.basics.virtdata.libimpl.discrete;

import io.basics.virtdata.libimpl.ThreadSafeHash;

import java.util.function.DoubleToIntFunction;
import java.util.function.LongUnaryOperator;

public class IntegerSampler implements LongUnaryOperator {

    private final DoubleToIntFunction f;
    private ThreadSafeHash hash;

    public IntegerSampler(DoubleToIntFunction parentFunc, boolean hash) {
        this.f = parentFunc;
        if (hash) {
            this.hash = new ThreadSafeHash();
        }
    }

    @Override
    public long applyAsLong(long value) {
        if (hash!=null) {
            value = hash.applyAsLong(value);
        }
        double unit = (double) value / (double) Long.MAX_VALUE;
        int sample =f.applyAsInt(unit);
        return sample;
    }
}
