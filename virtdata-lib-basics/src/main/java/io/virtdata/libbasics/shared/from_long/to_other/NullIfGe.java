package io.virtdata.libbasics.shared.from_long.to_other;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
@Categories(Category.nulls)
public class NullIfGe implements Function<Long,Long> {

    private final long gevalue;

    public NullIfGe(long gevalue) {
        this.gevalue = gevalue;
    }

    @Override
    public Long apply(Long aLong) {
        if (aLong >= gevalue) return null;
        return aLong;
    }
}