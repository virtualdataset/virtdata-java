package io.virtdata.docsys.metafs.core;

import java.util.Iterator;
import java.util.function.Function;

public class TransformingIterator<I,O> implements Iterator<O> {

    private final Function<I,? extends O> function;
    private Iterator<? extends I> wrapped;

    public TransformingIterator(Function<I,? extends O> function, Iterator<I> wrapped) {
        this.function = function;
        this.wrapped = wrapped;
    }

    @Override
    public boolean hasNext() {
        return wrapped.hasNext();
    }

    @Override
    public O next() {
        I next = wrapped.next();
        O applied = function.apply(next);
        return applied;
    }

//    @Override
//    public O next() {
//        O elem = function.apply(wrapped.next());
//        return elem;
//    }

}
