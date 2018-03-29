package io.virtdata.basicsmappers.from_long.to_string;

import io.virtdata.annotations.ThreadSafeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class ListTemplate implements LongFunction<List<String>> {

    private final LongToIntFunction sizeFunc;
    private final LongFunction<String> valueFunc;

    public ListTemplate(LongToIntFunction sizeFunc,
                        LongFunction<String> valueFunc) {
        this.sizeFunc = sizeFunc;
        this.valueFunc = valueFunc;
    }

    @Override
    public List<String> apply(long value) {
        int size = sizeFunc.applyAsInt(value);
        List<String> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(valueFunc.apply(value+i));
        }
        return list;
    }
}
