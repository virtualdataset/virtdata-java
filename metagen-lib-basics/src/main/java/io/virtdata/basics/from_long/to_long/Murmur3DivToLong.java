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

package io.virtdata.basics.from_long.to_long;

import de.greenrobot.common.hash.Murmur3F;
import io.basics.virtdata.api.ThreadSafeMapper;

import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class Murmur3DivToLong implements LongUnaryOperator {

    private ThreadLocal<Murmur3F> murmur3f_TL = ThreadLocal.withInitial(Murmur3F::new);
    private DivideToLong divideToLongMapper;

    public Murmur3DivToLong(long divisor) {
        this.divideToLongMapper = new DivideToLong(divisor);
    }

    @Override
    public long applyAsLong(long input) {
        long divided= divideToLongMapper.applyAsLong(input);
        Murmur3F murmur3f = murmur3f_TL.get();
        murmur3f.update((int) (divided % Integer.MAX_VALUE));
        return murmur3f.getValue();
    }
}
