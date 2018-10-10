package io.virtdata.templates;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringCompositorTest {

    @Test
    public void testBasicSegment() {
        StringCompositor c = new StringCompositor("A");
        assertThat(c.parseSection("A")).containsExactly("A");
        assertThat(c.parseSection("A{B}C")).containsExactly("A","B","C");
    }

    @Test
    public void testBasicSegmentEscape() {
        StringCompositor c = new StringCompositor("A");
        assertThat(c.parseSection("A\\{{B}C")).containsExactly("A{","B","C");
    }

}