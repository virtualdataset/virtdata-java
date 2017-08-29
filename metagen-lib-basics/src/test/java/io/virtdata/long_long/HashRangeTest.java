package io.virtdata.long_long;

import io.virtdata.from_long.to_long.HashRange;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HashRangeTest {

    @Test
    public void testFixedSize() {
        HashRange hashRange = new HashRange(65);
        for (int i = 0; i < 10; i++) {
            long l = hashRange.applyAsLong(i);
            assertThat(l).isEqualTo(65);
        }
    }

}