package io.virtdata.basicsmappers.unary_int;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
public class ToInt implements Function<Object,Integer> {

    @Override
    public Integer apply(Object o) {
        return Integer.valueOf(o.toString());
    }
}
