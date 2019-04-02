package io.virtdata.testmappers;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class TestableTemplateTest {

    public void testTestableTemplateValidForTesting() {
        TestableTemplate tt = new TestableTemplate(",", String::valueOf, String::valueOf);
        String v = tt.apply(3);
        assertThat(v).isEqualTo("3,3");
    }

}