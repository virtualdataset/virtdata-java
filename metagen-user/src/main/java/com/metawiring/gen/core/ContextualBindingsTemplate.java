package com.metawiring.gen.core;

import com.metawiring.gen.metagenapi.ValuesArrayBinder;

/**
 * A template that maps a set of generator specs, a context object, and a method for applying
 * generated values to the context object. This can be used in the configuration phase, in global
 * scope without triggering generator bindings resolution.
 *
 * @param <C> The type of the contextual template object.
 * @param <R> The type which will be produced when generated values are applied to a type C
 */
public class ContextualBindingsTemplate<C, R> {

    private C context;
    private BindingsTemplate bindingsTemplate;
    private ValuesArrayBinder<C, R> valuesArrayBinder;

    public ContextualBindingsTemplate(C context,
                                      BindingsTemplate bindingsTemplate,
                                      ValuesArrayBinder<C, R> valuesArrayBinder) {
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

    public ValuesArrayBinder<C, R> getValuesArrayBinder() {
        return valuesArrayBinder;
    }

    public ContextualBindings<C, R> resolveBindings() {
        Bindings bindings = bindingsTemplate.resolveBindings();
        return new ContextualBindings<C, R>(bindings, context, valuesArrayBinder);
    }

}
