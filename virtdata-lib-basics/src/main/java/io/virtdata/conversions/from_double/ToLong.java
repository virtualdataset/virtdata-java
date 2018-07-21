package io.virtdata.conversions.from_double;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.DoubleToLongFunction;

@ThreadSafeMapper
@Categories({Category.conversion})
public class ToLong implements DoubleToLongFunction {

    private final long scale;

    public ToLong(long scale) {
        this.scale = scale;
    }

    public ToLong() {
        this.scale = Long.MAX_VALUE;
    }

    @Override
    public long applyAsLong(double input) {
        return (long) (input % scale);
    }
}
