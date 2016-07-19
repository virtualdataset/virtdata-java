package io.virtdata.gen.internal;

import org.apache.commons.math3.distribution.IntegerDistribution;

import java.util.function.LongUnaryOperator;

/**
 * <p>Thread Local Real Distribution Adapter, with deferred construction.</p>
 * <p>This class uses thread local instances of both the distribution and the
 * random bypass adapter. While this may seem wasteful, it provides a thread-safe
 * way to utilize all the apache commons math stat curves. A future implementation
 * may simply implement just the necessary curve sampling logic to avoid this ugliness.</p>
 */
public class TLIDA<T extends IntegerDistribution> implements LongUnaryOperator {

    private ThreadLocalIDA tlida;

    public TLIDA(String... args) {
        tlida = new ThreadLocalIDA(args);
    }

    @Override
    public long applyAsLong(long operand) {
        long result = tlida.get().applyAsLong(operand);
        return result;
    }

    private static class ThreadLocalIDA extends ThreadLocal<IntegerDistributionAdapter> {

        private final DeferredConstructor<IntegerDistribution> idistConstructor;
        IntegerDistributionAdapter ida;

        ThreadLocalIDA(String... args) {
            ConstructorResolver.resolve(args).construct();
            idistConstructor = ConstructorResolver.resolve(args);
        }

        @Override
        protected IntegerDistributionAdapter initialValue() {
            IntegerDistribution idist = idistConstructor.construct();
            return new IntegerDistributionAdapter(idist);
        }
    }

}