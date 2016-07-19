package io.virtdata.libraryimpl;

public interface FunctionComposer<T> {
    FunctionComposer andThen(Object outer);
    T getComposedFunction();
}
