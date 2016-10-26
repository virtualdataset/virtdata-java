package io.virtdata.templates;

import io.virtdata.core.Bindings;
import io.virtdata.core.BindingsTemplate;

public class StringBindingsTemplate {

    private String stringTemplate;
    private BindingsTemplate bindingsTemplate;

    public StringBindingsTemplate(String stringTemplate, BindingsTemplate bindingsTemplate) {
        this.stringTemplate = stringTemplate;
        this.bindingsTemplate = bindingsTemplate;
    }

    public StringBindings resolve() {
        Bindings bindings = bindingsTemplate.resolveBindings();
        StringCompositor compositor = new StringCompositor(stringTemplate);
        return new StringBindings(compositor,bindings);
    }
}
