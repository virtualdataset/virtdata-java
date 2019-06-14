package io.virtdata.libbasics.shared.from_long.to_other;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
@Categories(Category.nulls)
public class NullIfEq implements Function<Long,Long> {

    private final long eqvalue;

    public NullIfEq(long eqvalue) {
        this.eqvalue = eqvalue;
    }

    @Override
    public Long apply(Long aLong) {
        if (aLong.equals(eqvalue)) return null;
        return aLong;
    }
}