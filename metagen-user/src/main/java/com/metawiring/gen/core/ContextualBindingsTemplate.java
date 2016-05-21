package com.metawiring.gen.core;

import com.metawiring.gen.metagenapi.ValuesArrayBinder;

public class ContextualBindingsTemplate<C,T> {

    private C context;
    private BindingsTemplate bindingsTemplate;
    private ValuesArrayBinder<C,T> valuesArrayBinder;

    public ContextualBindingsTemplate(C context,
                                      BindingsTemplate bindingsTemplate,
                                      ValuesArrayBinder<C, T> valuesArrayBinder) {
        this.context = context;
        this.bindingsTemplate = bindingsTemplate;
        this.valuesArrayBinder = valuesArrayBinder;
    }

    public C getContext() {
        return context;
    }

    public BindingsTemplate getBindingsTemplate() {
        return bindingsTemplate;
    }

    public ValuesArrayBinder<C,T> getValuesArrayBinder() {
        return valuesArrayBinder;
    }

    public ContextualBindings<C,T> resolveBindings() {
        Bindings bindings = bindingsTemplate.resolveBindings();
        return new ContextualBindings<C,T>(bindings, context, valuesArrayBinder);
    }

}
