package io.virtdata.libbasics.shared.from_long.to_string;

import io.virtdata.libbasics.shared.from_long.to_collection.MapTemplate;
import org.assertj.core.api.MapAssert;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MapTemplateTest {

    @Test
    public void testStringStringMap() {
        MapTemplate mt = new MapTemplate(true, (i) -> (int) i, (j) -> j, (k) -> k);
        Map<Object, Object> m1 = mt.apply(3L);
        assertThat(m1).containsKeys("3","4","5");
        assertThat(m1).containsValues("3","4","5");

    }

    @Test
    public void testLongDoubleMap() {
        MapTemplate mt = new MapTemplate(
                false,
                (i) -> (int)i,
                (j) -> j,
                k -> (double) k
        );
        MapAssert<Object, Object> m2 = assertThat(mt.apply(4L));
        assertThat(m2.containsOnlyKeys(4L,5L,6L,7L));
        assertThat(m2.containsValues(4.0D,5.0D,6.0D,7.0D));
    }

}