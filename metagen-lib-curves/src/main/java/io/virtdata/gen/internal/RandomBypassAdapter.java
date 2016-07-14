package io.virtdata.gen.internal;

import org.apache.commons.math3.random.AbstractRandomGenerator;

/**
 * This is just a way to make apache commons math distributions work with
 * an externally mediated RNG source (precooked hash outputs).
 * It's ugly, and it will be rewritten. It is _NOT_ threadsafe.
 * At least it is contained here in its wicked collusion with the sampling adapter.
 *
 * This is a loopback that is used ONLY as state for side-effects, so it shouldn't
 * be used for anything except as an API work-around for the sampling adapter to
 * use distributions from apache commons math with repeatable outputs for a given samplerFunction.
 *
 * Basically, it allows manual control of the RNG input to a distribution function.
 */
public class RandomBypassAdapter extends AbstractRandomGenerator {

    private long seed;

    /**
     * This is expected to be set with a hashed value before being accessed.
     * @param seed - The hashed input
     */
    @Override
    public void setSeed(long seed) {
        this.seed= seed;
    }

    @Override
    public double nextDouble() {
        // Because of IEEE 754, you should let the ALU do it's work to yield this value,
        // Sure, there's an off by 1 precision error here, but who's counting 1/2^64
        // The precision bias is likely to be a worse issue (1.0 and 0.0 vs 0.5)
        // There are no shortcuts for this unless you are using the Int form, which will
        // still suffer from precision bias.
        double next = ((double) seed ) / ((double) Long.MAX_VALUE);
        return next;
    }
}
