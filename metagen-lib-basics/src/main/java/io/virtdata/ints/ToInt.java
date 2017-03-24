package io.virtdata.ints;

import java.util.function.Function;

public class ToInt implements Function<Object,Integer> {

    @Override
    public Integer apply(Object o) {
        return Integer.valueOf(o.toString());
    }
}
