package io.virtdata.basicsmappers.from_long.to_collection;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.basicsmappers.from_long.to_long.HashRange;
import io.virtdata.basicsmappers.from_long.to_string.HashedLineToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

@ThreadSafeMapper
@Categories({Category.collections})
public class HashedLineToStringList implements LongFunction<List> {

    private final HashedLineToString hashedLineToString;
    private final HashRange hashRange;

    public HashedLineToStringList(String filename, int minSize, int maxSize) {
        this.hashedLineToString = new HashedLineToString(filename);
        this.hashRange = new HashRange(minSize,maxSize);
    }

    @Override
    public List apply(long value) {
        long size = hashRange.applyAsLong(value);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(hashedLineToString.apply(value+i));
        }
        return list;
    }
}
