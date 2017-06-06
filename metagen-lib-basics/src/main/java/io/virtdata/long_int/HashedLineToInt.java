package io.virtdata.long_int;

import io.virtdata.util.ResourceFinder;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.LongToIntFunction;

public class HashedLineToInt implements LongToIntFunction {
    private final static Logger logger = LoggerFactory.getLogger(HashedLineToInt.class);

    private final List<String> lines;

    private final MersenneTwister rng;
    private final IntegerDistribution itemDistribution;
    private final String filename;

    public HashedLineToInt(String filename) {
        this(filename, System.nanoTime());
    }

    public HashedLineToInt(String filename, long seed) {
        this.filename = filename;
        this.lines = ResourceFinder.readDataFileLines(filename);
        this.rng = new MersenneTwister(seed);
        this.itemDistribution= new UniformIntegerDistribution(rng, 0, lines.size()-2);
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + filename;
    }

    @Override
    public int applyAsInt(long value) {
        int itemIdx = itemDistribution.sample();
        String item = lines.get(itemIdx);
        return Integer.valueOf(item);
    }
}

