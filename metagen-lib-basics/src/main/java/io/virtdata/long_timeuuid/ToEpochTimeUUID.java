package io.virtdata.long_timeuuid;

import io.virtdata.long_long.HostHash;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;

import java.util.UUID;
import java.util.function.LongFunction;


/**
 *
 * Converts a long UTC timestamp in epoch millis form into a Version 1 TimeUUID
 * according to <a href="https://www.ietf.org/rfc/rfc4122.txt">RFC 4122</a>.
 * This means that only one unique value for a timeuuid can be generated for
 * each epoch milli value, even though version 1 TimeUUIDs can normally represent
 * up to 10000 distinct values per millisecond. If you need to access this
 * level of resolution for testing purposes, use {@link ToFinestTimeUUID}
 * instead. This method is to support simple mapping to natural timestamps
 * as we often find in the real world.
 */
public class ToEpochTimeUUID implements LongFunction<UUID> {

    private final long node;
    private final long clock;
    private static DateTime calendarStart =
            new DateTime(1582, 10, 15, 0, 0,
                    DateTimeZone.UTC);


    /**
     * Create version 1 timeuuids with a specific static node and specific clock data.
     * This is useful for testing so that you can know that values are verifiable, even though
     * in non-testing practice, you would rely on some form of entropy per-system to provide
     * more practical dispersion of values over reboots, etc.
     *
     * @param node  a fixture value for testing that replaces node bits
     * @param clock a fixture value for testing that replaces clock bits
     */
    public ToEpochTimeUUID(long node, long clock) {
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
    public ToEpochTimeUUID(long node) {
        this.node = node;
        this.clock = 0L;
    }

    /**
     * Create version 1 timeuuids with a per-host node and empty clock data.
     * The node and clock components are seeded from network interface data. In this case,
     * the clock data is not seeded uniquely.
     */
    public ToEpochTimeUUID() {
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

    private static long timeClicksFor(long epochMillis) {
        Duration duration = new Duration(calendarStart, new DateTime(epochMillis, DateTimeZone.UTC));
        return duration.getMillis() * 10000; // 100ns intervals since 1582
    }

    @Override
    public UUID apply(long timeClicks) {
        return new UUID(msbBitsFor(timeClicksFor(timeClicks)), lsbBitsFor(node, clock));
    }


}
