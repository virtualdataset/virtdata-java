package io.virtdata.mappers.hashed_discrete;

import io.virtdata.libimpl.RandomBypassAdapter;
import io.virtdata.reflection.ConstructorResolver;
import io.virtdata.reflection.DeferredConstructor;
import org.apache.commons.math3.distribution.IntegerDistribution;

import java.util.Arrays;

/**
 * Construct a template for mapping an integer distribution and a per-instance
 * bypass companion object.
 */
public class IDistHashedResolver {

    private final String[] args;
    private DeferredConstructor<? extends IntegerDistribution> deferredConstructor;

    public IDistHashedResolver(Class<? extends IntegerDistribution> distClass, String... args) {
        this.args = args;
        this.deferredConstructor = ConstructorResolver.resolve(distClass,args);
    }
    public IDistHashedResolver(String... args) {
        this.args = args;
        this.deferredConstructor = ConstructorResolver.resolve(args);
    }

    public IDistHashedCoupler resolve() {
        RandomBypassAdapter bypass = new RandomBypassAdapter();
        IntegerDistribution idist = deferredConstructor.prefixArgs(bypass).construct();
        return new IDistHashedCoupler(idist, bypass);
    }

    public String toString() {
        return IDistHashedResolver.class.getSimpleName() + ": args:" + Arrays.toString(args);
    }

    /**
     * Provide a way to reuse the Integer Distribution Resolver for each thread.
     */
    public static class ThreadSafe extends ThreadLocal<IDistHashedCoupler> {

        private final IDistHashedResolver idistResolver;

        public ThreadSafe(String... args) {
            this.idistResolver = new IDistHashedResolver(args);
        }

        @Override
        protected IDistHashedCoupler initialValue() {
            return idistResolver.resolve();
        }
    }
}
