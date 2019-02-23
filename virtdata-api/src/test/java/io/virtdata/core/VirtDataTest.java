package io.virtdata.core;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VirtDataTest {

    @Test
    public void testBasicBindings() {
        BindingsTemplate bt = VirtData.getTemplate("a","Mod(5)");
        assertThat(bt).isNotNull();
    }

}