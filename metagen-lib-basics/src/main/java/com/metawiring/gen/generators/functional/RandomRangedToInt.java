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

package com.metawiring.gen.generators.functional;

import com.metawiring.gen.metagenapi.Generator;
import org.apache.commons.math3.random.MersenneTwister;

public class RandomRangedToInt implements Generator<Integer> {
    private MersenneTwister theTwister = new MersenneTwister(System.nanoTime());
    private long min;
    private long max;
    private long _length;

    public RandomRangedToInt(int min, int max) {
        if (max<=min) {
            throw new RuntimeException("max must be >= min");
        }
        this.min = min;
        this.max = max;
        this._length = max - min;
    }

    public RandomRangedToInt(String min, String max) {
        this(Integer.valueOf(min), Integer.valueOf(max));
    }

    @Override
    public Integer get(long input) {
        long value = Math.abs(theTwister.nextLong());
        value %= _length;
        value += min;
        return (int) value;
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + min + ":" + max;
    }

}
