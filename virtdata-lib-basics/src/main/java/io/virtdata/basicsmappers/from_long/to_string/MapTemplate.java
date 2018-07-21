package io.virtdata.basicsmappers.from_long.to_string;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;

/**
 * Construct a {@code Map<String,String>} from a set of input functions
 * which determine the size of the map, the value of each key, and
 * the value for that key. In between calling the key and value functions,
 * the input value is incremented.
 */
@ThreadSafeMapper
public class MapTemplate implements LongFunction<Map<String,String>> {

    private final LongToIntFunction sizeFunc;
    private final LongFunction<String> keyFunc;
    private final LongFunction<String> valueFunc;

    @Example({"MapTemplate(HashRange(3-7),NumberNameToString(),LastNames())","create maps between 3 and 7 values big, " +
            "with number names as the keys, and last names as the values"})
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
