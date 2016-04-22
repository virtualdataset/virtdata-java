package com.metawiring.gen.core;

import com.metawiring.gen.metagenapi.ValuesBinder;

public abstract class ContextualBindingsTemplate<C,T> {

    private C context;
    private BindingsTemplate bindingsTemplate;
    private ValuesBinder<C,T> valuesBinder;

    public ContextualBindingsTemplate(C context,
                                      BindingsTemplate bindingsTemplate,
                                      ValuesBinder<C, T> valuesBinder) {
        this.context = context;
        this.bindingsTemplate = bindingsTemplate;
        this.valuesBinder = valuesBinder;
    }

    public C getContext() {
        return context;
    }

    public BindingsTemplate getBindingsTemplate() {
        return bindingsTemplate;
    }

    public ValuesBinder<C,T> getValuesBinder() {
        return valuesBinder;
    }

    public ContextualBindings<C,T> resolveBindings() {
        Bindings bindings = bindingsTemplate.resolveBindings();
        return new ContextualBindings<C,T>(bindings, context, valuesBinder);
    }

}
