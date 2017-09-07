package io.basics.virtdata.api;

/**
 * <p>The BindableType combines both the ability to bind a set of mapped values to a
 * specific target type with the ability to return the resulting composite object.
 * Having both in one type allows you to implement a bindable template pattern that
 * can then be used with data mapping templates.</p>
 *
 * @param <T> The template type
 * @param <R> The result type that binding values will yield
 */
public interface BindableType<T,R> extends ValuesArrayBinder<T,R>, Binder<R> {
}
