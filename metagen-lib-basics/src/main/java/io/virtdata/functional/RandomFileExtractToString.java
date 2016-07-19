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

import io.virtdata.util.FileReaders;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.CharBuffer;
import java.util.function.LongFunction;

public class RandomFileExtractToString implements LongFunction<String> {

    private final static Logger logger = LoggerFactory.getLogger(RandomFileExtractToString.class);
    private static CharBuffer fileDataImage =null;

    private int minsize, maxsize;
    private MersenneTwister rng = new MersenneTwister(System.nanoTime());
    private IntegerDistribution sizeDistribution;
    private IntegerDistribution positionDistribution;
    private String fileName;

    public RandomFileExtractToString(String fileName, int minsize, int maxsize) {
        this.fileName = fileName;
        this.minsize = minsize;
        this.maxsize = maxsize;
    }

    @Override
    public String apply(long input) {

        if (fileDataImage == null) {
            synchronized (RandomFileExtractToString.class) {
                if (fileDataImage == null) {
                    CharBuffer image= FileReaders.loadFileToCharBuffer(fileName);
                    fileDataImage = image;

                }
            }
        }

        if (sizeDistribution==null)
        {
            sizeDistribution = new UniformIntegerDistribution(rng, minsize, maxsize);
            positionDistribution = new UniformIntegerDistribution(rng, 1, fileDataImage.limit() - maxsize);
        }

        int offset = positionDistribution.sample();
        int length = sizeDistribution.sample();
        String sub = null;
        try {
            sub = fileDataImage.subSequence(offset, offset + length).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sub;

    }


    public String toString() {
        return getClass().getSimpleName() + ":" + minsize + ":" + maxsize;
    }

}
