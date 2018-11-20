package io.virtdata.conversions.from_double;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.DoubleFunction;

@Categories({Category.conversion})
@ThreadSafeMapper
public class ToString implements DoubleFunction<String> {

    public String apply(double v) {
        return String.valueOf(v);
    }
}
