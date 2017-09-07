package io.basics.virtdata.templates;

import io.basics.virtdata.core.Bindings;
import io.basics.virtdata.core.BindingsTemplate;

public class CSVBindingsTemplate {

    private BindingsTemplate bindingsTemplate;

    public CSVBindingsTemplate(BindingsTemplate bindingsTemplate) {
        this.bindingsTemplate = bindingsTemplate;
    }

    public CSVBindings resolve() {
        Bindings bindings = bindingsTemplate.resolveBindings();
        return new CSVBindings(bindings);
    }
}
