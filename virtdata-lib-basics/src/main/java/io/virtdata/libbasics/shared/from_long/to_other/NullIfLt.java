package io.virtdata.libbasics.shared.from_long.to_other;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
@Categories(Category.nulls)
public class NullIfLt implements Function<Long,Long> {

    private final long ltvalue;

    public NullIfLt(long ltvalue) {
        this.ltvalue = ltvalue;
    }

    @Override
    public Long apply(Long aLong) {
        if (aLong < ltvalue) return null;
        return aLong;
    }
}