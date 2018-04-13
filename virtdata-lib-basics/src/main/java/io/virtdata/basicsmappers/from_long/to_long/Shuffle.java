/*
 *   Copyright 2018 jshook
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.Description;
import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.lfsrs.MetaShift;

/**
 * This function provides a low-overhead shuffling effect without loading
 * elements into memory. It uses a bundled dataset of pre-computed
 * Galois LFSR shift register configurations, along with a down-sampling
 * method to provide amortized virtual shuffling with minimal memory usage.
 */
@ThreadSafeMapper
@Description("Provides virtual shuffling extremely large numbers.")
@Example("Shuffle(11,99) // Provide all values between 11 and 98 inclusive, in some order, then repeat")
public class Shuffle extends MetaShift.Func {

    private final long max;
    private final long min;
    private final long size;
//    public int[] stats = new int[1];


    public Shuffle(long min, long maxPlusOne) {
        this(min, maxPlusOne, Integer.MAX_VALUE);
    }

    public Shuffle(long min, long maxPlusOne, int moduloSelector) {
        super(MetaShift.Masks.forPeriodAndBankModulo((maxPlusOne-min),moduloSelector));
//        System.out.println("galois:" + MetaShift.Masks.forPeriodAndBankModulo((maxPlusOne-min),Integer.MAX_VALUE));
        this.min = min;
        this.max = maxPlusOne;
        this.size = (max-min);
    }

    @Override
    public long applyAsLong(long register) {
//        System.out.format("%4d ",register);
        register = (register % size) +1;
//        System.out.format("%4d ",register);
        register = super.applyAsLong(register);
//        System.out.format("%4d ",register);
//        int resample=1;
        while (register>size) {
//            resample++;
            register = super.applyAsLong(register);
//            System.out.format("(%4d)",register);
        }
//        if (stats.length<resample+1) {
//            stats=Arrays.copyOf(stats,resample+1);
//        }
//        stats[resample]++;
        register+=min;
//        System.out.println();
        return register;
    }

}
