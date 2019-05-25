package io.virtdata.libbasics.shared.from_long.to_collection;

import io.virtdata.libbasics.shared.from_long.to_int.HashRange;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListTest {

    @Test
    public void testList() {
        List lf = new List(new HashRange(1, 3), (l) -> "_" + l);
        java.util.List<Object> l1 = lf.apply(2L);
        assertThat(l1).containsExactly("_2","_3");
    }

    @Test
    public void testStringList() {
        StringList slf = new StringList((s) -> 2,(v)->v);
        java.util.List<String> sl1 = slf.apply(13L);
        assertThat(sl1).containsExactly("13","14");
    }
}