package io.virtdata.datamappers.functions;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.stathelpers.aliasmethod.WeightedStrings;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class FirstNames extends WeightedStrings implements LongFunction<String> {

    /**
     * WeightedStrings("Name", "Weight", "data/female_firstnames", "data/male_firstnames");
     */
    public FirstNames() {
        super("Name", "Weight", "data/female_firstnames", "data/male_firstnames");
    }
}
