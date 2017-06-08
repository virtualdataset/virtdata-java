package io.virtdata.long_collections;

import io.virtdata.long_long.HashRange;
import io.virtdata.long_string.HashedLineToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

public class HashedLineToList implements LongFunction<List> {

    private final HashedLineToString hashedLineToString;
    private final HashRange hashRange;

    public HashedLineToList(String filename, int minSize, int maxSize) {
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
