package io.virtdata.mappers.continuous;

import io.virtdata.mappers.internal.RandomBypassAdapter;
import io.virtdata.reflection.ConstructorResolver;
import io.virtdata.reflection.DeferredConstructor;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.Arrays;
import java.util.function.LongToDoubleFunction;

/**
 * <p>Thread Local Integer Distribution Adapter, with deferred construction.</p>
 * <p>This class uses thread local instances of both the distribution and the
 * random bypass adapter. While this may seem wasteful, it provides a thread-safe
 * way to utilize all the apache commons math stat curves. A future implementation
 * may simply implement just the necessary curve sampling logic to avoid this ugliness.</p>
 */
public class CDistThreadLocal<T extends RealDistribution> implements LongToDoubleFunction {

    private final String[] args;
    private ThreadLocalCDistInverter cDistInverter;

    public CDistThreadLocal(String... args) {
        cDistInverter = new ThreadLocalCDistInverter(args);
        this.args = args;
    }

    @Override
    public double applyAsDouble(long value) {
        double result = cDistInverter.get().applyAsDouble(value);
        return result;
    }

    public String toString() {
        return CDistThreadLocal.class.getSimpleName() + ": " + Arrays.toString(args);
    }

    private static class ThreadLocalCDistInverter extends ThreadLocal<CDistInverter> {

        private final DeferredConstructor<RealDistribution> rdistConstructor;

        ThreadLocalCDistInverter(String... args) {
            rdistConstructor = ConstructorResolver.resolve(args);
        }

        @Override
        protected CDistInverter initialValue() {
            RandomBypassAdapter bypass = new RandomBypassAdapter();
            RealDistribution rdist = rdistConstructor.prefixArgs(bypass).construct();
            return new CDistInverter(rdist);
        }
    }
}