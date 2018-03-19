package io.virtdata.long_string;

import io.virtdata.basicsmappers.from_long.to_string.HashedFileExtractToString;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class HashedFileExtractToStringTest {

    @Test
    public void testHashedFileBasic() {
        HashedFileExtractToString extract =
                new HashedFileExtractToString("data/lorem_ipsum_full.txt", 3, 3000);
        for (long cycle = 0; cycle < 50000; cycle++) {
            String apply = extract.apply(cycle);
            assertThat(apply.length()).isGreaterThanOrEqualTo(3);
            assertThat(apply.length()).isLessThanOrEqualTo(3000);
        }

    }
}