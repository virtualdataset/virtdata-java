package io.virtdata.basicsmappers.from_long.to_string;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;

/**
 * Construct a {@code Map} from a set of input functions.
 * In the full four-argument form, the initial boolean argument
 * specifies whether to convert the key and value objects to String form
 * before adding them to the map. The condensed three-argument assumes that
 * a string to string map is desired by default.
 *
 * The last three parameters specify functions for the size, key, and values
 * of the map to be created. For each map created, the size is determined
 * with the size function. Then, a key and value are created for that many
 * entries using the original input value plus a relative index for both
 * the key and value functions respectively.
 */
@ThreadSafeMapper
public class MapTemplate implements LongFunction<Map<Object,Object>> {

    private final LongToIntFunction sizeFunc;
    private final LongFunction<Object> keyFunc;
    private final LongFunction<Object> valueFunc;
    private final boolean stringify;

    @Example({"MapTemplate(HashRange(3,7),NumberNameToString(),LastNames())","create maps between 3 and 7 values big, " +
            "with number names as the keys, and last names as the values"})
    public MapTemplate(LongToIntFunction sizeFunc,
                       LongFunction<Object> keyFunc,
                       LongFunction<Object> valueFunc) {
        this.sizeFunc = sizeFunc;
        this.stringify=true;
        this.keyFunc = keyFunc;
        this.valueFunc = valueFunc;
    }

    @Example({"MapTemplate(false,HashRange(3,7),NumberNameToString(),HashRange(1300,1700))",
    "create a map of size 3-7 entries, with a key of type string and a value of type int (Integer by autoboxing)"})
    public MapTemplate(boolean stringify,
                       LongToIntFunction sizeFunc,
                       LongFunction<Object> keyFunc,
                       LongFunction<Object> valueFunc) {
        this.sizeFunc = sizeFunc;
        this.stringify=stringify;
        this.keyFunc = keyFunc;
        this.valueFunc = valueFunc;
    }

    @Override
    public Map<Object,Object> apply(long value) {
        int size = sizeFunc.applyAsInt(value);
        HashMap<Object, Object> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Object keyObject = keyFunc.apply(value + i);
            Object valueObject = valueFunc.apply(value+i);
            if (stringify) {
                map.put(String.valueOf(keyObject),String.valueOf(valueObject));
            } else {
                map.put(keyObject,valueObject);
            }
        }
        return map;
    }
}
