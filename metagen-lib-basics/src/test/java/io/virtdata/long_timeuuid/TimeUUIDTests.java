package io.virtdata.long_timeuuid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeUUIDTests {

    @Test
    public void testFinestTimeUUID() {
        ToFinestTimeUUID tjtu = new ToFinestTimeUUID(234,567);
        UUID uuid = tjtu.apply(0);
        assertThat(uuid.node()).isEqualTo(234);
        assertThat(uuid.clockSequence()).isEqualTo(567);
        assertThat(uuid.timestamp()).isEqualTo(0L);
        uuid = tjtu.apply(5);
        assertThat(uuid.timestamp()).isEqualTo(5L);
    }

    @Test
    public void testEpochTimeUUID() {
        ToEpochTimeUUID tetu = new ToEpochTimeUUID(123,456);
        UUID uuid = tetu.apply(0);
        assertThat(uuid.node()).isEqualTo(123L);
        assertThat(uuid.clockSequence()).isEqualTo(456);
        DateTime gregs =
                new DateTime(
                        1582,
                        10,
                        15,
                        0,
                        0,
                        DateTimeZone.UTC);
        Interval interval = new Interval(gregs, new DateTime(0));
        long expected = interval.toDuration().getMillis() * 10000;
        assertThat(uuid.timestamp()).isEqualTo(expected);
    }

    @Test
    public void sanityCheckHostGen() {
        ToFinestTimeUUID tjtu = new ToFinestTimeUUID();
        UUID withHostData = tjtu.apply(0);
        assertThat(withHostData.node()).isNotEqualTo(0L);
    }
}