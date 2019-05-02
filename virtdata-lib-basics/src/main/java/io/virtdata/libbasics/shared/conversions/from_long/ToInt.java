package io.virtdata.libbasics.shared.conversions.from_long;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.LongToIntFunction;

/**
 * Convert the input value to a long.
 */
@ThreadSafeMapper
@Categories({Category.conversion})
public class ToInt implements LongToIntFunction {

    private final int scale;

    public ToInt(int scale) {
        this.scale = scale;
    }

    public ToInt() {
        this.scale = Integer.MAX_VALUE;
    }

    @Override
    public int applyAsInt(long input) {
        return (int) (input % scale);
    }
}
