package io.virtdata.templates;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class StringCompositorTest {

    @Test
    public void testCompositeEvenTwo() {
        StringCompositor sc = new StringCompositor("{} two {} four");
        String s = sc.bindValues(sc, new Object[]{"one", "three"});
        assertThat(s).isEqualTo("one two three four");
    }

    @Test
    public void testCompositeOddBraces() {
        StringCompositor sc = new StringCompositor("{} two {}");
        String s = sc.bindValues(sc, new Object[]{"one", "three"});
        assertThat(s).isEqualTo("one two three");
    }

    @Test
    public void testCompositeOddLiterals() {
        StringCompositor sc = new StringCompositor("two {} four");
        String s = sc.bindValues(sc, new Object[]{"three"});
        assertThat(s).isEqualTo("two three four");
    }

}