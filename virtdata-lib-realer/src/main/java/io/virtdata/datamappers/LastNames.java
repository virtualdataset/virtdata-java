package io.virtdata.datamappers;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.stathelpers.aliasmethod.WeightedStrings;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class LastNames extends WeightedStrings implements LongFunction<String> {

    public LastNames() {
        super("Name", "prop100k", "data/surnames");
    }
}
