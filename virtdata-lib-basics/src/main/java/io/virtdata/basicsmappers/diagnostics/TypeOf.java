package io.virtdata.basicsmappers.diagnostics;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

/**
 * Yields the class of the resulting type in String form.
 */
@ThreadSafeMapper
public class TypeOf implements Function<Object,String> {

    @Override
    public String apply(Object o) {
        return o.getClass().toString();
    }


}
