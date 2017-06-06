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

package io.virtdata.long_map;


import io.virtdata.long_int.HashRange;
import io.virtdata.long_string.HashedLineToString;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongFunction;

public class HashedLineToStringMap implements LongFunction<Map<String,String>> {

    private final HashedLineToString lineDataMapper;
    private final HashRange sizeRange;

    public HashedLineToStringMap(String paramFile, int maxSize) {

        this.sizeRange = new HashRange(0, maxSize-1);
        this.lineDataMapper = new HashedLineToString(paramFile);
    }

    @Override
    public Map<String, String> apply(long input) {
        int mapSize = sizeRange.applyAsInt(input);
        Map<String,String> map = new HashMap<>();
        for (int idx=0;idx<mapSize;idx++) {
            map.put(lineDataMapper.apply(input), lineDataMapper.apply(input));
        }
        return map;
    }

}
