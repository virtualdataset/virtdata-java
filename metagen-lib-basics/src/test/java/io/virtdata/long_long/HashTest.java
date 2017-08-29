package io.virtdata.long_long;

import io.virtdata.from_long.to_long.Hash;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class HashTest {

    @Test
    public void testFunctionalResult() {
        Hash hash = new Hash();
        assertThat(hash.applyAsLong(0L)).isEqualTo(2945182322382062539L);
        assertThat(hash.applyAsLong(1L)).isEqualTo(6292367497774912474L);
        assertThat(hash.applyAsLong(2L)).isEqualTo(8218881827949364593L);
        assertThat(hash.applyAsLong(3L)).isEqualTo(8048510690352527683L);
        assertThat(hash.applyAsLong(0L)).isEqualTo(2945182322382062539L);
        assertThat(hash.applyAsLong(1L)).isEqualTo(6292367497774912474L);
        assertThat(hash.applyAsLong(2L)).isEqualTo(8218881827949364593L);
        assertThat(hash.applyAsLong(3L)).isEqualTo(8048510690352527683L);
    }

}