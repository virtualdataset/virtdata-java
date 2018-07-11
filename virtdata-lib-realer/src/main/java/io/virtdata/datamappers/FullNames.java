package io.virtdata.datamappers;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.basicsmappers.from_long.to_string.Template;

import java.util.function.LongFunction;

@ThreadSafeMapper
public class FullNames extends Template implements LongFunction<String> {

    public FullNames() {
        super("{} {}", new FirstNames(), new LastNames());
    }
}
