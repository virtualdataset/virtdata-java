package io.virtdata.core;

import io.virtdata.templates.StringCompositor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class StringCompositorTest {

    private BindingsTemplate template;
    private Bindings bindings;

    @BeforeClass
    public void setupTemplate() {

        BindingsTemplate bindingsTemplate = new BindingsTemplate(AllDataMapperLibraries.get());
        bindingsTemplate.addFieldBinding("ident","Identity()");
        bindingsTemplate.addFieldBinding("mod5", "Mod(5)");
        this.template = bindingsTemplate;
        this.bindings = bindingsTemplate.resolveBindings();
    }

    @Test
    public void testBindValues() {
        StringCompositor c = new StringCompositor("A{ident}C");
        String s = c.bindValues(c, bindings, 0L);
        assertThat(s).isEqualTo("A0C");
    }

}