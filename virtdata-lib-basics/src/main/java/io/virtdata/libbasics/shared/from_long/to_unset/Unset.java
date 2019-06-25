package io.virtdata.libbasics.shared.from_long.to_unset;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.api.VALUE;

import java.util.function.LongFunction;

@Categories(Category.nulls)
@ThreadSafeMapper
public class Unset implements LongFunction<Object> {
    @Override
    public Object apply(long value) {
        return VALUE.unset;
    }
}
