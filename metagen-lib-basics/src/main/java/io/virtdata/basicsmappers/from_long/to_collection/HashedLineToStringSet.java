package io.virtdata.basicsmappers.from_long.to_collection;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.basicsmappers.from_long.to_long.HashRange;
import io.virtdata.basicsmappers.from_long.to_string.HashedLineToString;

import java.util.HashSet;
import java.util.Set;
import java.util.function.LongFunction;

@ThreadSafeMapper
public class HashedLineToStringSet implements LongFunction<Set<String>> {

    private final HashedLineToString hashedLineToString;
    private final HashRange hashRange;

    public HashedLineToStringSet(String filename, int minSize, int maxSize) {
        this.hashedLineToString = new HashedLineToString(filename);
        this.hashRange = new HashRange(minSize,maxSize);
    }

    @Override
    public Set<String> apply(long value) {
        long size = hashRange.applyAsLong(value);
        Set<String> list = new HashSet<>();
        for (int i = 0; i < size; i++) {
            list.add(hashedLineToString.apply(value+i));
        }
        return list;
    }
}
