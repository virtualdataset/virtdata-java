package io.virtdata.mappers.hashed_continuous;

import io.virtdata.libimpl.RandomBypassAdapter;
import io.virtdata.reflection.ConstructorResolver;
import io.virtdata.reflection.DeferredConstructor;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.Arrays;

/**
 * Construct a template for mapping an integer distribution and a per-instance
 * bypass companion object.
 */
public class CDistHashedResolver {

    private final String[] args;
    private DeferredConstructor<RealDistribution> deferredConstructor;

    public CDistHashedResolver(String... args) {
        this.args = args;
        this.deferredConstructor = ConstructorResolver.resolve(args);
    }

    public CDistHashedCoupler resolve() {
        RandomBypassAdapter bypass = new RandomBypassAdapter();
        RealDistribution idist = deferredConstructor.prefixArgs(bypass).construct();
        return new CDistHashedCoupler(idist, bypass);
    }

    public String toString() {
        return CDistHashedResolver.class.getSimpleName() + ": args:" + Arrays.toString(args);
    }

    /**
     * Provide a way to reuse the Integer Distribution Resolver for each thread.
     */
    public static class ThreadSafe extends ThreadLocal<CDistHashedCoupler> {

        private final CDistHashedResolver cdistHashedResolver;

        public ThreadSafe(String... args) {
            this.cdistHashedResolver = new CDistHashedResolver(args);
        }

        @Override
        protected CDistHashedCoupler initialValue() {
            return cdistHashedResolver.resolve();
        }
    }
}
