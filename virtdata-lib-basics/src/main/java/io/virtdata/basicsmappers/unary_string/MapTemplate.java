package io.virtdata.basicsmappers.unary_string;

import io.virtdata.api.ThreadSafeMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class MapTemplate implements LongFunction<Map<String,String>> {

    private final LongToIntFunction sizeFunc;
    private final LongFunction<String> keyFunc;
    private final LongFunction<String> valueFunc;

    public MapTemplate(LongToIntFunction sizeFunc,
                       LongFunction<String> keyFunc,
                       LongFunction<String> valueFunc) {
        this.sizeFunc = sizeFunc;
        this.keyFunc = keyFunc;
        this.valueFunc = valueFunc;
    }

    @Override
    public Map<String,String> apply(long value) {
        int size = sizeFunc.applyAsInt(value);
        HashMap<String, String> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            map.put(keyFunc.apply(value+i),valueFunc.apply(value+i));
        }
        return map;
    }
}
