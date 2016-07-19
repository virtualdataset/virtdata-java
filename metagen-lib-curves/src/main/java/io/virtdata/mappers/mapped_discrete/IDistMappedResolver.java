package io.virtdata.mappers.mapped_discrete;

import io.virtdata.libimpl.RandomBypassAdapter;
import io.virtdata.reflection.ConstructorResolver;
import io.virtdata.reflection.DeferredConstructor;
import org.apache.commons.math3.distribution.IntegerDistribution;

import java.util.Arrays;

/**
 * Construct a template for mapping an integer distribution and a per-instance
 * bypass companion object.
 */
public class IDistMappedResolver {

    private final String[] args;
    private DeferredConstructor<? extends IntegerDistribution> deferredConstructor;

    public IDistMappedResolver(Class<? extends IntegerDistribution> distClass, String... args) {
        this.args = args;
        this.deferredConstructor = ConstructorResolver.resolve(distClass,args);
    }
    public IDistMappedResolver(String... args) {
        this.args = args;
        this.deferredConstructor = ConstructorResolver.resolve(args);
    }

    public IDistMappedCoupler resolve() {
        RandomBypassAdapter bypass = new RandomBypassAdapter();
        IntegerDistribution idist = deferredConstructor.prefixArgs(bypass).construct();
        return new IDistMappedCoupler(idist, bypass);
    }

    public String toString() {
        return IDistMappedResolver.class.getSimpleName() + ": args:" + Arrays.toString(args);
    }

    /**
     * Provide a way to reuse the Integer Distribution Resolver for each thread.
     */
    public static class ThreadSafe extends ThreadLocal<IDistMappedCoupler> {

        private final IDistMappedResolver idistMappedResolver;

        public ThreadSafe(String... args) {
            this.idistMappedResolver = new IDistMappedResolver(args);
        }

        @Override
        protected IDistMappedCoupler initialValue() {
            return idistMappedResolver.resolve();
        }
    }
}
