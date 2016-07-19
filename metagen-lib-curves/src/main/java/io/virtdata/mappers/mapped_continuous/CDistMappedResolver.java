package io.virtdata.mappers.mapped_continuous;

import io.virtdata.libimpl.RandomBypassAdapter;
import io.virtdata.reflection.ConstructorResolver;
import io.virtdata.reflection.DeferredConstructor;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.Arrays;

/**
 * Construct a template for mapping an integer distribution and a per-instance
 * bypass companion object.
 */
public class CDistMappedResolver {

    private final String[] args;
    private DeferredConstructor<RealDistribution> deferredConstructor;

    public CDistMappedResolver(String... args) {
        this.args = args;
        this.deferredConstructor = ConstructorResolver.resolve(args);
    }

    public CDistMappedCoupler resolve() {
        RandomBypassAdapter bypass = new RandomBypassAdapter();
        RealDistribution idist = deferredConstructor.prefixArgs(bypass).construct();
        return new CDistMappedCoupler(idist, bypass);
    }

    public String toString() {
        return CDistMappedResolver.class.getSimpleName() + ": args:" + Arrays.toString(args);
    }

    /**
     * Provide a way to reuse the Integer Distribution Resolver for each thread.
     */
    public static class ThreadSafe extends ThreadLocal<CDistMappedCoupler> {

        private final CDistMappedResolver cdistMappedResolver;

        public ThreadSafe(String... args) {
            this.cdistMappedResolver = new CDistMappedResolver(args);
        }

        @Override
        protected CDistMappedCoupler initialValue() {
            return cdistMappedResolver.resolve();
        }
    }
}
