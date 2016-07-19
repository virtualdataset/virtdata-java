package io.virtdata.gen.internal;

import org.apache.commons.math3.distribution.RealDistribution;

import java.util.function.LongToDoubleFunction;

/**
 * <p>TLIDA: ThreadLocal IntegerDistribution Adapter with deferred construction</p>
 * <p>
 * Generates a long value representing a sampled value from a cumulative
 * density function for a binomial curve.
 * <p>
 * <p>This class uses thread local instances of both the distribution and the
 * random bypass adapter. While this may seem wasteful, it provides a thread-safe
 * way to utilize all the apache commons math stat curves. A future implementation
 * may simply implement just the necessary curve sampling logic to avoid this ugliness.</p>
 */
public class TLRDA<T extends RealDistribution> implements LongToDoubleFunction {

    private ThreadLocalRDA tlrda;

    public TLRDA(String... args) {
        tlrda = new ThreadLocalRDA(args);
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
            RealDistribution rdist = rdistConstructor.construct();
            return new RealDistributionAdapter(rdist);
        }
    }

}