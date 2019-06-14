package io.virtdata.libbasics.shared.from_long.to_other;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
@Categories(Category.nulls)
public class NullIfGt implements Function<Long,Long> {

    private final long eqvalue;

    public NullIfGt(long gtvalue) {
        this.eqvalue = gtvalue;
    }

    @Override
    public Long apply(Long aLong) {
        if (aLong >eqvalue) return null;
        return aLong;
    }
}