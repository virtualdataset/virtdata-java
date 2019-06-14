package io.virtdata.libbasics.shared.from_double.to_other;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;

import java.util.function.Function;

@ThreadSafeMapper
@Categories(Category.nulls)
public class NullIfWithin implements Function<Double,Double> {


    private final double min;
    private final double max;

    public NullIfWithin(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public Double apply(Double value) {
        if (value>=min && value <=max) { return null; }
        return value;
    }
}