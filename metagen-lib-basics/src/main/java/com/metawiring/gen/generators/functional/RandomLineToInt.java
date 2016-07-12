package com.metawiring.gen.generators.functional;

import com.metawiring.gen.metagenapi.Generator;
import com.metawiring.gen.util.FileReaders;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RandomLineToInt implements Generator<Integer> {
    private final static Logger logger = LoggerFactory.getLogger(RandomLineToString.class);

    private List<String> lines = new ArrayList<>();

    MersenneTwister rng = new MersenneTwister(System.nanoTime());
    private IntegerDistribution itemDistribution;
    private String filename;

    public RandomLineToInt(String filename) {
        this.filename = filename;
        this.lines = FileReaders.loadToStringList(filename);
        itemDistribution= new UniformIntegerDistribution(rng, 0, lines.size()-2);
    }

    @Override
    public Integer get(long input) {
        int itemIdx = itemDistribution.sample();
        String item = lines.get(itemIdx);
        return Integer.valueOf(item);
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + filename;
    }

}

