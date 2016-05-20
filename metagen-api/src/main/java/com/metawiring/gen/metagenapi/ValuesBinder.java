package com.metawiring.gen.metagenapi;

public interface ValuesBinder<S, R> {

    /**
     * Using context instance of type S, AKA the source, create and bind values to
     * target object of type R
     * @param context A context object that knows how to provide an instance of type R
     * @param values An array of values which should be bound to the new R instance
     * @return The new result instance of R
     */
    R bindValues(S context, Object[] values);
}
