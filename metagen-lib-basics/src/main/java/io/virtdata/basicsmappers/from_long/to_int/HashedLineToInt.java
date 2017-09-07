package io.virtdata.basicsmappers.from_long.to_int;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.util.ResourceFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.LongToIntFunction;

@ThreadSafeMapper
public class HashedLineToInt implements LongToIntFunction {
    private final static Logger logger = LoggerFactory.getLogger(HashedLineToInt.class);

    private final List<String> lines;
    private final String filename;
    private final Hash intHash;

    public HashedLineToInt(String filename) {
        this.filename = filename;
        this.lines = ResourceFinder.readDataFileLines(filename);
        this.intHash = new Hash();
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + filename;
    }

    @Override
    public int applyAsInt(long value) {
        int itemIdx = intHash.applyAsInt(value) % lines.size();
        String item = lines.get(itemIdx);
        return Integer.valueOf(item);
    }
}

