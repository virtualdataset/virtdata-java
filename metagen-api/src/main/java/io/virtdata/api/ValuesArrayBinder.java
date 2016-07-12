package io.virtdata.api;

/**
 * ValuesArrayBinder provides a way to apply an array of object values to a template
 * object of type S to yield a new object instance of type R. The object array is
 * a positional argument list. There is no named-argument facility.
 *
 * <p>Parameter Examples:
 * <ul>
 *     <LI>S: prepared Statement, R: bound statement</LI>
 *     <LI>S: string template, R: interpolated string value</LI>
 * </ul>
 * </p>
 *
 * @param <T> The template type
 * @param <R> The result type
 */
public interface ValuesArrayBinder<T, R> {

    /**
     * Using context instance of type S, AKA the template, create and bind values to
     * target object of type R
     * @param context A context object that knows how to provide an instance of type R
     * @param values An array of values which should be bound to the new R instance
     * @return The new result instance of R
     */
    R bindValues(T context, Object[] values);
}
