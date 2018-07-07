package io.virtdata;

import io.virtdata.api.DataMapper;
import io.virtdata.core.VirtData;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Test(enabled = false)
public class FuncTemplateTest {

    @Test
    public void testCombinationsTemplateValues() {
        Optional<DataMapper<String>> fto = VirtData.getMapper("FuncTemplate(\"c1([[Combinations('A-Z')]])\")");
        assertThat(fto).isPresent();
        String letter13 = fto.get().get(13L);
        assertThat(letter13).isEqualTo("c1(N)");
    }

    @Test
    public void testStringsAndCombos() {
        Optional<DataMapper<String>> fto = VirtData.getMapper(
                "FuncTemplate(\"[[Combinations('0-9;a-z')]]-[[WeightedStrings('foo:0.33;bar:0.33;baz:0.34')]]\")");
        assertThat(fto).isPresent();
        Assertions.assertThat(fto.get().get(43L)).isEqualTo("1r-foo");
        Assertions.assertThat(fto.get().get(4300L)).isEqualTo("5k-baz");
    }

}