package io.virtdata.libbasics.shared.unary_string;

import io.virtdata.libbasics.shared.from_long.to_string.Template;
import org.testng.annotations.Test;

import java.util.function.LongFunction;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class TemplateTest {

    public void testTemplate() {
        Template t = new Template("{}-->{}{}", new F("={}="), new F("_{}_"), new F("<{}>"));
        assertThat(t.apply(6L)).isEqualTo("=6=-->_7_<8>");
    }

    public void testExtraCurlyBraces() {
        Template t = new Template("{{}-->{}{}}", new F("={}="), new F("_{}_"), new F("<{}>"));
        assertThat(t.apply(6L)).isEqualTo("{=6=-->_7_<8>}");
    }



    private static class F implements LongFunction<String> {

        private final String template;

        public F(String template) {
            this.template = template;
        }

        @Override
        public String apply(long value) {
            return template.replaceAll("\\{}", String.valueOf(value));
        }
    }

}