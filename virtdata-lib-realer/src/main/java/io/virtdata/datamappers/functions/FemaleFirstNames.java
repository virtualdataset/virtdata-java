package io.virtdata.datamappers.functions;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.stathelpers.aliasmethod.WeightedStrings;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class FemaleFirstNames extends WeightedStrings implements LongFunction<String> {

    public FemaleFirstNames() {
        super("Name", "Weight", "data/female_firstnames");
    }
}
