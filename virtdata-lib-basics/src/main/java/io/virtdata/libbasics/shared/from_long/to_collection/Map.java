package io.virtdata.libbasics.shared.from_long.to_collection;

import io.virtdata.annotations.*;

import java.util.HashMap;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;

/**
 * Create a {@code Map} from a long input based on three functions,
 * the first to determine the map size, and the second to populate
 * the map with key objects, and the third to populate the map with
 * value objects. The long input fed to the second and third functions
 * is incremented between entries.
 *
 * To directly create Maps with key and value Strings using the same
 * mapping functions, simply use {@link StringMap} instead.
 */
@Categories({Category.collections})
@ThreadSafeMapper
public class Map implements LongFunction<java.util.Map<Object, Object>> {

    private final LongToIntFunction sizeFunc;
    private final LongFunction<Object> keyFunc;
    private final LongFunction<Object> valueFunc;

    @Example({"MapTemplate(false,HashRange(3,7),NumberNameToString(),HashRange(1300,1700))",
            "create a map of size 3-7 entries, with a key of type " +
                    "string and a value of type int (Integer by autoboxing)"})
    public Map(LongToIntFunction sizeFunc,
               LongFunction<Object> keyFunc,
               LongFunction<Object> valueFunc) {
        this.sizeFunc = sizeFunc;
        this.keyFunc = keyFunc;
        this.valueFunc = valueFunc;
    }

    @Override
    public java.util.Map<Object, Object> apply(long value) {
        int size = sizeFunc.applyAsInt(value);
        HashMap<Object, Object> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Object keyObject = keyFunc.apply(value + i);
            Object valueObject = valueFunc.apply(value + i);
            map.put(keyObject, valueObject);
        }
        return map;
    }
}
