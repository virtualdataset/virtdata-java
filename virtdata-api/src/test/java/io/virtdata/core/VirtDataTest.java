package io.virtdata.core;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class VirtDataTest {

    @Test
    public void testBasicBindings() {
        Map<String,String> specs = new HashMap<String,String>() {{
            put("a","Mod(5)");
        }};

        BindingsTemplate bt = VirtData.getTemplate(specs);
        assertThat(bt).isNotNull();
    }


}