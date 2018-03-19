package io.virtdata;

import io.virtdata.api.DataMapper;
import io.virtdata.core.VirtData;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Test
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

        @Test
        public void testMultipleAndCompose(){
            Optional<DataMapper<String>> fto = VirtData.getMapper(

            "FuncTemplate('{\"q\":\"content:\"[[HashedFileExtractToString(\"data/lorem_ipsum_full.txt\",10,11)]]\" AND contentid:[[compose ToEpochTimeUUID(); ToString() -> String]]\"}')");
            assertThat(fto).isPresent();
            String s = fto.get().get(43L);
            assertThat(s).isEqualTo("{\"q\":\"content:\"licitudin \" AND contentid:1387cfb0-1dd2-11b2-8000-000000000000\"}");
        }

}