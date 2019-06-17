package io.virtdata.libbasics.shared.from_string.to_unset;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@Categories(Category.nulls)
@ThreadSafeMapper
public class NullIfNullOrEmpty implements Function<String,String> {
    
    @Override
    public String apply(String s) {
        if (s==null || s.isEmpty()) {
            return null;
        }
        return s;
    }
}
