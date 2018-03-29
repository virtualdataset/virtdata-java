package io.virtdata.datamappers.functions;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.stathelpers.aliasmethod.WeightedStrings;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class MaleFirstNames extends WeightedStrings implements LongFunction<String> {

    public MaleFirstNames() {
        super("Name", "Weight", "data/male_firstnames");
    }
}
