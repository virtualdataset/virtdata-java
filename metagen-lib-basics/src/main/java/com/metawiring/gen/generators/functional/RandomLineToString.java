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
import com.metawiring.gen.util.FileReaders;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RandomLineToString implements Generator<String> {
    private final static Logger logger = LoggerFactory.getLogger(RandomLineToString.class);

    private List<String> lines = new ArrayList<>();

    MersenneTwister rng = new MersenneTwister(System.nanoTime());
    private IntegerDistribution itemDistribution;
    private String filename;

    public RandomLineToString(String filename) {
        this.filename = filename;
        this.lines = FileReaders.loadToStringList(filename);
        itemDistribution= new UniformIntegerDistribution(rng, 0, lines.size()-2);
    }

    @Override
    public String get(long input) {
        int itemIdx = itemDistribution.sample();
        String item = lines.get(itemIdx);
        return item;
    }

    public String toString() {
        return getClass().getSimpleName() + ":" + filename;
    }

}
