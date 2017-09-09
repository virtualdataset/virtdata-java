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

}