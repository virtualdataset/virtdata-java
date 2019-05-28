package io.virtdata.libbasics.shared.from_long.to_collection;

import io.virtdata.annotations.*;

import java.util.HashMap;
import java.util.function.LongFunction;
import java.util.function.LongToIntFunction;
/**
 * Create a {@code Map<String,String>} from a long input
 * based on three functions,
 * the first to determine the map size, and the second to populate
 * the map with key objects, and the third to populate the map with
 * value objects. The long input fed to the second and third functions
 * is incremented between entries. Regardless of the object type provided
 * by the second and third functions, {@link java.lang.Object#toString()}
 * is used to determine the key and value to add to the map.
 *
 * To create Maps of any key and value types, simply use {@link Map} with
 * an specific key and value mapping functions.
 */

@Categories({Category.collections})
@ThreadSafeMapper
public class StringMap implements LongFunction<java.util.Map<String, String>> {

    private final LongToIntFunction sizeFunc;
    private final LongFunction<Object> keyFunc;
    private final LongFunction<Object> valueFunc;

    @Example({"MapTemplate(false,HashRange(3,7),NumberNameToString(),HashRange(1300,1700))",
            "create a map of size 3-7 entries, with a key of type " +
                    "string and a value of type int (Integer by autoboxing)"})
    public StringMap(LongToIntFunction sizeFunc,
                     LongFunction<Object> keyFunc,
                     LongFunction<Object> valueFunc) {
        this.sizeFunc = sizeFunc;
        this.keyFunc = keyFunc;
        this.valueFunc = valueFunc;
    }

    @Override
    public java.util.Map<String,String> apply(long value) {
        int size = sizeFunc.applyAsInt(value);
        HashMap<String, String> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Object keyObject = keyFunc.apply(value + i);
            Object valueObject = valueFunc.apply(value + i);
            map.put(keyObject.toString(), valueObject.toString());
        }
        return map;
    }
}
