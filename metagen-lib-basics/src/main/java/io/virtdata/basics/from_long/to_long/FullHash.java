package io.virtdata.basics.from_long.to_long;

import de.greenrobot.common.hash.Murmur3F;
import io.basics.virtdata.api.ThreadSafeMapper;

import java.nio.ByteBuffer;
import java.util.function.LongUnaryOperator;

/**
 * This uses the Murmur3F (64-bit optimized) version of Murmur3,
 * not as a checksum, but as a simple hash. It doesn't bother
 * pushing the high-64 bits of input, since it only uses the lower
 * 64 bits of output.
 *
 * This version returns the value regardless of this sign bit.
 * It does not return the absolute value, as {@link Hash} does.
 */
@ThreadSafeMapper
public class FullHash implements LongUnaryOperator {

    private ThreadLocal<State> state_TL = ThreadLocal.withInitial(State::new);

    @Override
    public long applyAsLong(long value) {
        State state = state_TL.get();
        state.murmur3F.reset();
        state.bb.putLong(0,value);
        state.murmur3F.update(state.bb.array(),0,Long.BYTES);
        return state.murmur3F.getValue();
    }

    private static class State {
        ByteBuffer bb = ByteBuffer.allocate(Long.BYTES);
        Murmur3F murmur3F = new Murmur3F();
    }
}
