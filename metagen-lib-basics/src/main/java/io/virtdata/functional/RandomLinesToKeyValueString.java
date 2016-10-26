/*
 *
 *       Copyright 2015 Jonathan Shook
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package io.virtdata.functional;

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.Map;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

public class RandomLinesToKeyValueString implements LongFunction<String> {

    private final RandomLineToStringMap lineDataMapper;
    private RandomLineToString paramNameMapper;
    private IntegerDistribution sizeDistribution;
    private MersenneTwister rng = new MersenneTwister(System.nanoTime());

    public RandomLinesToKeyValueString(String paramFile, int sizeDistribution) {
        lineDataMapper = new RandomLineToStringMap(paramFile, sizeDistribution);
    }

    @Override
    public String apply(long input) {
        Map<String, String> stringStringMap = lineDataMapper.apply(input);
        String mapstring = stringStringMap.entrySet().stream().
                map(es -> es.getKey() + ":" + es.getValue() + ";")
                .collect(Collectors.joining());
        return mapstring;
    }

}
