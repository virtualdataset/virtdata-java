package io.virtdata.libbasics.shared.from_string.to_unset;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

/**
 * Yields a null if the input String is empty. Throws an error if the input
 * String is null.
 */
@Categories(Category.nulls)
@ThreadSafeMapper
public class NullIfEmpty implements Function<String,String> {

    @Override
    public String apply(String s) {
        if (s!=null && s.isEmpty()) {
            return null;
        }
        if (s!=null) {
            return s;
        }
        throw new RuntimeException("This function is not able to take null values as input. If you need to do that, consider using NullIfNullOrEmpty()");
    }
}
