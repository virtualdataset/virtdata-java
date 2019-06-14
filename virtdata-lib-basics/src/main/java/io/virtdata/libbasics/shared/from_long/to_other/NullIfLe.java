package io.virtdata.libbasics.shared.from_long.to_other;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
@Categories(Category.nulls)
public class NullIfLe implements Function<Long,Long> {

    private final long levalue;

    public NullIfLe(long levalue) {
        this.levalue = levalue;
    }

    @Override
    public Long apply(Long aLong) {
        if (aLong <= levalue) return null;
        return aLong;
    }
}