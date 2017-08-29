package io.virtdata.from_long.to_time_types;

import io.virtdata.DateTimeFormats;
import io.virtdata.api.Example;
import io.virtdata.api.ThreadSafeMapper;
import org.joda.time.DateTime;

import java.util.function.LongUnaryOperator;

/**
 * This function sets the minimum long value to the equivalent
 * unix epoch time in milliseconds. It simply adds the input
 * value to this base value as determined by the provided
 * time specifier. It wraps any overflow within this range as well.
 */
@Example("StartingEpochMillis('2017-01-01T23:59:59')")
@Example("StartingEpochMillis('2012',12345)")
@Example("StartingEpochMillis('20171231T1015.243',123,456)")
@ThreadSafeMapper
public class StartingEpochMillis implements LongUnaryOperator {

    private final DateTime startingTime;
    private final long startingUnixEpochMillis;
    private final long headroom;

    public StartingEpochMillis(String baseTimeSpec) {
        startingTime = DateTimeFormats.parseEpochTimeToDateTime(baseTimeSpec);
        startingUnixEpochMillis = startingTime.getMillis();
        headroom = Long.MAX_VALUE - startingUnixEpochMillis;
    }

    @Override
    public long applyAsLong(long value) {
        return startingUnixEpochMillis + (value % headroom);
    }
}
