package io.virtdata.templates;

import io.virtdata.core.Bindings;
import io.virtdata.core.BindingsTemplate;

/**
 * Uses a string template and a bindings template to create instances of {@link StringBindings}.
 */
public class StringBindingsTemplate {

    private String stringTemplate;
    private BindingsTemplate bindingsTemplate;

    public StringBindingsTemplate(String stringTemplate, BindingsTemplate bindingsTemplate) {
        this.stringTemplate = stringTemplate;
        this.bindingsTemplate = bindingsTemplate;
    }

    /**
     * Create a new instance of {@link StringBindings}, preferably in the thread context that will use it.
     * @return a new StringBindings
     */
    public StringBindings resolve() {
        Bindings bindings = bindingsTemplate.resolveBindings();
        StringCompositor compositor = new StringCompositor(stringTemplate);
        return new StringBindings(compositor,bindings);
    }
}
