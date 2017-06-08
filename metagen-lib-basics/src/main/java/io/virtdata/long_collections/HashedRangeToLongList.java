package io.virtdata.long_collections;

import io.virtdata.long_long.HashRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

public class HashedRangeToLongList implements LongFunction<List<Long>> {

    private final HashRange valueRange;
    private final HashRange sizeRange;

    public HashedRangeToLongList(int minVal, int maxVal, int minSize, int maxSize) {
        if (minSize>=maxSize || minSize>=maxSize) {
            throw new RuntimeException("HashedRangeToLongList must have minval, maxval, minsize, maxsize, where min<max.");
        }
        this.valueRange = new HashRange(minVal,maxVal);
        this.sizeRange = new HashRange(minSize,maxSize);
    }

    @Override
    public List<Long> apply(long value) {
        long listSize = sizeRange.applyAsLong(value);
        ArrayList<Long> longList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            longList.add(valueRange.applyAsLong(value+i));
        }
        return longList;
    }
}
