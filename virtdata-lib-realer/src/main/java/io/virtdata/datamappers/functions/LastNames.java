package io.virtdata.datamappers.functions;

import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.stathelpers.aliasmethod.WeightedStrings;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class LastNames extends WeightedStrings implements LongFunction<String> {

    public LastNames() {
        super("Name", "prop100k", "data/surnames");
    }
}
