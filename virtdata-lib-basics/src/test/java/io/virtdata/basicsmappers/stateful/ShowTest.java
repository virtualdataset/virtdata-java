package io.virtdata.basicsmappers.stateful;

import io.virtdata.basicsmappers.from_long.to_long.Save;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class ShowTest {

    @Test(singleThreaded = true)
    public void testBasicStateSupport() {
        new Clear().applyAsLong(0L);
        io.virtdata.basicsmappers.from_long.to_long.Save saveFoo = new io.virtdata.basicsmappers.from_long.to_long.Save("foo");
        saveFoo.applyAsLong(23);
        new Save("cycle").applyAsLong(-1L);
        Show showAll=new Show();
        String shown = showAll.apply(234L);
        assertThat(shown).isEqualTo("{foo=23, cycle=-1}");
        io.virtdata.basicsmappers.unary_string.Save saveBar = new io.virtdata.basicsmappers.unary_string.Save("bar");
        saveBar.apply("Bar");
        Show showFoo = new Show("foo");
        Show showBar = new Show("bar");
        assertThat(showFoo.apply(2342343L)).isEqualTo("{foo=23}");
        assertThat(showBar.apply(23423L)).isEqualTo("{bar=Bar}");
        new Clear().applyAsLong(234);
        assertThat(showAll.apply("234").isEmpty());
    }


}