package io.basics.virtdata.core;

import io.basics.virtdata.api.ValuesMapBinder;

/**
 * A template that maps a set of specifiers, a context object, and a method for applying
 * mapped values to the context object. This can be used in the configuration phase, in global
 * scope without triggering mapper bindings resolution from specifiers.
 *
 * @param <C> The type of the contextual template object.
 * @param <R> The type which will be produced when mapped values are applied to a type C
 */
public class ContextualBindingsMapTemplate<C, R> {

    private C context;
    private BindingsTemplate bindingsTemplate;
    private ValuesMapBinder<C, R> valuesArrayBinder;

    public ContextualBindingsMapTemplate(C context,
                                         BindingsTemplate bindingsTemplate,
                                         ValuesMapBinder<C, R> valuesMapBinder) {
        this.context = context;
        this.bindingsTemplate = bindingsTemplate;
        this.valuesArrayBinder = valuesMapBinder;
    }

    public C getContext() {
        return context;
    }

    public BindingsTemplate getBindingsTemplate() {
        return bindingsTemplate;
    }

    public ValuesMapBinder<C, R> getValuesArrayBinder() {
        return valuesArrayBinder;
    }

    public ContextualMapBindings<C, R> resolveBindings() {
        Bindings bindings = bindingsTemplate.resolveBindings();
        return new ContextualMapBindings<C, R>(bindings, context, valuesArrayBinder);
    }

}
