package io.virtdata.templates;

import io.virtdata.api.Binder;
import io.virtdata.core.Bindings;

public class StringBindings implements Binder<String> {

    private final StringCompositor compositor;
    private Bindings bindings;

    public StringBindings(StringCompositor compositor, Bindings bindings) {
        this.compositor = compositor;
        this.bindings = bindings;
    }

    @Override
    public String bind(long value) {
        Object[] allValues = bindings.getAll(value);
        String s = compositor.bindValues(compositor, allValues);
        return s;
    }
}
