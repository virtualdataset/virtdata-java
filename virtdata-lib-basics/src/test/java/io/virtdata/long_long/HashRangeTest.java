package io.virtdata.long_long;

import io.virtdata.basicsmappers.from_long.to_long.HashRange;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashRangeTest {

    @Test
    public void testFixedSize() {
        HashRange hashRange = new HashRange(65);
        assertThat(hashRange.applyAsLong(32L)).isEqualTo(11L);
    }

}