package io.virtdata.long_string;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class FuncTemplateTest {

    @Test
    public void testCombinationsTemplateValues() {
        FuncTemplate ft = new FuncTemplate("c1([[Combinations('A-Z')]])");
        String letter13 = ft.apply(13L);
        assertThat(letter13).isEqualTo("c1(N)");
    }

    @Test
    public void testStringsAndCombos() {
        FuncTemplate ft = new FuncTemplate("[[Combinations(0-9;a-z)]]-[[WeightedStrings(foo:0.33;bar:0.33;baz:0.34)]]");
        assertThat(ft.apply(43L)).isEqualTo("1r-foo");
        assertThat(ft.apply(4300L)).isEqualTo("5k-baz");
    }

}