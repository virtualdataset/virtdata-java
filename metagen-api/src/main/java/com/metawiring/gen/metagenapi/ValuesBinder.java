package com.metawiring.gen.metagenapi;

public interface ValuesBinder<C,T> {

    /**
     * Using context instance of type C, create and bind values to target object of type T
     * @param context A context object that knows how to provide an instance of type T
     * @param values An array of values which should be bound to the new T instance
     * @return The new instance of T
     */
    T bindValues(C context,Object[] values);
}
