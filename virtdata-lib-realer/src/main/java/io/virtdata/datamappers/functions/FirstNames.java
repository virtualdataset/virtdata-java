package io.virtdata.datamappers.functions;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.stathelpers.aliasmethod.WeightedStrings;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class FirstNames extends WeightedStrings implements LongFunction<String> {

    public FirstNames() {
        super("Name", "Weight", "data/female_firstnames", "data/male_firstnames");
    }
}
