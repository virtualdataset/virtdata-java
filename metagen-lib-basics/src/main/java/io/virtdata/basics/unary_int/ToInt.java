package io.virtdata.basics.unary_int;

import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToInt implements Function<Object,Integer> {

    @Override
    public Integer apply(Object o) {
        return Integer.valueOf(o.toString());
    }
}
