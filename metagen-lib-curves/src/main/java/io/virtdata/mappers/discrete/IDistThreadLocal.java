package io.virtdata.mappers.discrete;

import io.virtdata.mappers.internal.RandomBypassAdapter;
import io.virtdata.mappers.continuous.CDistThreadLocal;
import io.virtdata.reflection.ConstructorResolver;
import io.virtdata.reflection.DeferredConstructor;
import org.apache.commons.math3.distribution.IntegerDistribution;

import java.util.Arrays;
import java.util.function.LongUnaryOperator;

/**
 * <p>Thread Local Real Distribution Adapter, with deferred construction.</p>
 * <p>This class uses thread local instances of both the distribution and the
 * random bypass adapter. While this may seem wasteful, it provides a thread-safe
 * way to utilize all the apache commons math stat curves. A future implementation
 * may simply implement just the necessary curve sampling logic to avoid this ugliness.</p>
 */
public class IDistThreadLocal<T extends IntegerDistribution> implements LongUnaryOperator {

    private final String[] args;
    private ThreadLocalIDistInverter iDistInverter;

    public IDistThreadLocal(String... args) {
        iDistInverter = new ThreadLocalIDistInverter(args);
        this.args = args;
    }

    @Override
    public long applyAsLong(long operand) {
        long result = iDistInverter.get().applyAsLong(operand);
        return result;
    }

    public String toString() {
        return CDistThreadLocal.class.getSimpleName() + ": " + Arrays.toString(args);
    }


    private static class ThreadLocalIDistInverter extends ThreadLocal<IDistInverter> {

        private final DeferredConstructor<IntegerDistribution> idistConstructor;
        IDistInverter ida;

        ThreadLocalIDistInverter(String... args) {
            ConstructorResolver.resolve(args).construct();
            idistConstructor = ConstructorResolver.resolve(args);
        }

        @Override
        protected IDistInverter initialValue() {
            RandomBypassAdapter bypass = new RandomBypassAdapter();
            IntegerDistribution idist = idistConstructor.prefixArgs(bypass).construct();
            return new IDistInverter(idist);
        }
    }

}