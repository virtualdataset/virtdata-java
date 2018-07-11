package io.virtdata.continuous;

import io.virtdata.discrete.ThreadSafeHash;

import java.util.function.DoubleUnaryOperator;
import java.util.function.IntToDoubleFunction;

public class RealIntDoubleSampler implements IntToDoubleFunction {

    private final DoubleUnaryOperator f;
    private ThreadSafeHash hash;

    public RealIntDoubleSampler(DoubleUnaryOperator parentFunc, boolean hash) {
        this.f = parentFunc;
        if (hash) {
            this.hash = new ThreadSafeHash();
        }
    }

    @Override
    public double applyAsDouble(int input) {
        long value = input;
        if (hash!=null) {
            value = hash.applyAsLong(value);
        }
        double unit = (double) value / (double) Long.MAX_VALUE;
        double sample =f.applyAsDouble(unit);
        return sample;
    }
}
