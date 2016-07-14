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

import de.greenrobot.common.hash.Murmur3F;

import java.util.function.LongFunction;

public class Murmur3DivToString implements LongFunction<String> {

    private Murmur3F murmur3f = new Murmur3F();
    private DivideToLong longDivSequenceGenerator;

    public Murmur3DivToString(long divisor) {
        this.longDivSequenceGenerator = new DivideToLong(divisor);
    }
    public Murmur3DivToString(String divisor) {
        this(Long.valueOf(divisor));
    }

    @Override
    public String apply(long input) {
        long divided= longDivSequenceGenerator.applyAsLong(input);
        murmur3f.update((int) (divided % Integer.MAX_VALUE));
        return String.valueOf(murmur3f.getValue());
    }

}
