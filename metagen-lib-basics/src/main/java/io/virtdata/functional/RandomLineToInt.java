package io.virtdata.functional;

import io.virtdata.util.FileReaders;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongToIntFunction;

public class RandomLineToInt implements LongToIntFunction {
    private final static Logger logger = LoggerFactory.getLogger(RandomLineToInt.class);

    private List<String> lines = new ArrayList<>();

    MersenneTwister rng = new MersenneTwister(System.nanoTime());
    private IntegerDistribution itemDistribution;
    private String filename;

    public RandomLineToInt(String filename) {
        this.filename = filename;
        this.lines = FileReaders.loadToStringList(filename);
        itemDistribution= new UniformIntegerDistribution(rng, 0, lines.size()-2);
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

