package com.metawiring.gen.core;

import com.metawiring.gen.metagenapi.ValuesBinder;

public class ContextualBindings<C,T> {

    private final C context;
    private Bindings bindings;
    private ValuesBinder<C,T> valuesBinder;

    public ContextualBindings(Bindings bindings, C context, ValuesBinder<C,T> valuesBinder) {
        this.bindings = bindings;
        this.context = context;
        this.valuesBinder = valuesBinder;
    }

    public Bindings getBindings() {
        return bindings;
    }

    public C getContext() {
        return context;
    }

    public T bind(long value) {
        Object[] allGeneratedValues = bindings.getAll(value);
        T bound= valuesBinder.bindValues(context,allGeneratedValues);
        return bound;
    }
}
