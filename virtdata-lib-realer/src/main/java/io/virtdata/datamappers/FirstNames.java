package io.virtdata.datamappers;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.stathelpers.aliasmethod.WeightedStrings;

import java.util.function.LongFunction;

/**
 * Returns a first name from all names that were seen more than 100 times in the last census,
 * according to the frequency that it was actually seen.
 * This function does *not* pre-hash its input. You need to provide a form of hashing using
 * any of the documented Hash functions first.
 */
@ThreadSafeMapper
public class FirstNames extends WeightedStrings implements LongFunction<String> {

    @Example({"FirstNames()"})
    public FirstNames() {
        super("Name", "Weight", "data/female_firstnames", "data/male_firstnames");
    }
}
