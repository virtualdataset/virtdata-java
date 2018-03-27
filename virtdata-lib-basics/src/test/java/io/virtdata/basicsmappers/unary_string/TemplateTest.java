package io.virtdata.basicsmappers.unary_string;

import io.virtdata.basicsmappers.from_long.to_string.Template;
import org.testng.annotations.Test;

import java.util.function.LongFunction;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateTest {

    @Test
    public void testTemplate() {
        Template t = new Template("{}-->{}{}", new F("={}="), new F("_{}_"), new F("<{}>"));
        assertThat(t.apply(6L)).isEqualTo("=6=-->_6_<6>");
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