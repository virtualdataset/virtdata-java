package io.virtdata.gen.internal;

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
public class TLRDA<T extends RealDistribution> implements LongToDoubleFunction {

    private final String[] args;
    private ThreadLocalRDA tlrda;

    public TLRDA(String... args) {
        tlrda = new ThreadLocalRDA(args);
        this.args = args;
    }

    @Override
    public double applyAsDouble(long value) {
        double result = tlrda.get().applyAsDouble(value);
        return result;
    }

    private static class ThreadLocalRDA extends ThreadLocal<RealDistributionAdapter> {

        private final DeferredConstructor<RealDistribution> rdistConstructor;

        ThreadLocalRDA(String... args) {
            rdistConstructor= ConstructorResolver.resolve(args);
        }

        @Override
        protected RealDistributionAdapter initialValue() {
            RandomBypassAdapter bypass = new RandomBypassAdapter();
            RealDistribution rdist = rdistConstructor.prefixArgs(bypass).construct();
            return new RealDistributionAdapter(rdist);
        }
    }

    public String toString() {
        return TLRDA.class.getSimpleName() + ": " + Arrays.toString(args);
    }
}