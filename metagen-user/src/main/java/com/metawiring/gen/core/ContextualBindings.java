package com.metawiring.gen.core;

import com.metawiring.gen.metagenapi.ValuesArrayBinder;

public class ContextualBindings<C, T> {

    private final C context;
    private Bindings bindings;
    private ValuesArrayBinder<C, T> valuesArrayBinder;

    public ContextualBindings(Bindings bindings, C context, ValuesArrayBinder<C, T> valuesArrayBinder) {
        this.bindings = bindings;
        this.context = context;
        this.valuesArrayBinder = valuesArrayBinder;
    }

    public Bindings getBindings() {
        return bindings;
    }

    public C getContext() {
        return context;
    }

    public T bind(long value) {
        Object[] allGeneratedValues = bindings.getAll(value);
        try { // Provide bindings context data where it may be useful
            return valuesArrayBinder.bindValues(context, allGeneratedValues);
        } catch (Exception e) {
            throw new RuntimeException("Binding error:" + bindings.getTemplate().toString(allGeneratedValues), e);
        }

    }
}
