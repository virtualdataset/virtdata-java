package io.virtdata.long_timeuuid;

import io.virtdata.long_long.HostHash;

import java.util.UUID;
import java.util.function.LongFunction;

/**
 * Converts a count of 100ns intervals from 1582 Julian to a Type1 TimeUUID
 * according to <a href="https://www.ietf.org/rfc/rfc4122.txt">RFC 4122</a>.
 * This allows you to access the finest unit of resolution for the
 * purposes of simulating a large set of unique timeuuid values. This offers
 * 10000 times more unique values per ms than {@link ToEpochTimeUUID}.
 */
public class ToFinestTimeUUID implements LongFunction<UUID> {

    private final long node;
    private final long clock;

    /**
     * Create version 1 timeuuids with a specific static node and specific clock data.
     * This is useful for testing so that you can know that values are verifiable, even though
     * in non-testing practice, you would rely on some form of entropy per-system to provide
     * more practical dispersion of values over reboots, etc.
     *
     * @param node  a fixture value for testing that replaces node bits
     * @param clock a fixture value for testing that replaces clock bits
     */
    public ToFinestTimeUUID(long node, long clock) {
        this.node = node;
        this.clock = clock;
    }

    /**
     * Create version 1 timeuuids with a specific static node and empty clock data.
     * This is useful for testing so that you can know that values are verifiable, even though
     * in non-testing practice, you would rely on some form of entropy per-system to provide
     * more practical dispersion of values over reboots, etc.
     *
     * @param node a fixture value for testing that replaces node and clock bits
     */
    public ToFinestTimeUUID(long node) {
        this.node = node;
        this.clock = 0L;
    }

    /**
     * Create version 1 timeuuids with a per-host node and empty clock data.
     * The node and clock components are seeded from network interface data. In this case,
     * the clock data is not seeded uniquely.
     */
    public ToFinestTimeUUID() {
        this.node = new HostHash().applyAsLong(1);
        this.clock = 0L;
    }

    private static long msbBitsFor(long timeClicks) {
        return 0x0000000000001000L
                | (0x0fff000000000000L & timeClicks) >>> 48
                | (0x0000ffff00000000L & timeClicks) >>> 16
                | (0x00000000ffffffffL & timeClicks) << 32;
    }

    private static long lsbBitsFor(long node, long clock) {
        return ((clock & 0x0000000000003FFFL) << 48) | 0x8000000000000000L | node;
    }

    @Override
    public UUID apply(long timeClicks) {
        return new UUID(msbBitsFor(timeClicks), lsbBitsFor(node, clock));
    }


}
