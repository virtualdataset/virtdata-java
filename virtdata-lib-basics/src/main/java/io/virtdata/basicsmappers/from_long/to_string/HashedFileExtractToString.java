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

package io.virtdata.basicsmappers.from_long.to_string;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.basicsmappers.from_long.to_int.HashRange;
import io.virtdata.util.ResourceFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.CharBuffer;
import java.util.function.LongFunction;

@ThreadSafeMapper
public class HashedFileExtractToString implements LongFunction<String> {

    private final static Logger logger = LoggerFactory.getLogger(HashedFileExtractToString.class);
    private static CharBuffer fileDataImage =null;
    private final HashRange sizeRange;
    private final HashRange positionRange;

    private int minsize, maxsize;
    private final String fileName;

    public HashedFileExtractToString(String fileName, int minsize, int maxsize) {
        this.fileName = fileName;
        this.minsize = minsize;
        this.maxsize = maxsize;
        loadData();
        this.sizeRange = new HashRange(minsize, maxsize);
        this.positionRange = new HashRange(1, (fileDataImage.limit()-maxsize)-1);
    }

    private void loadData() {
        if (fileDataImage == null) {
            synchronized (HashedFileExtractToString.class) {
                if (fileDataImage == null) {
                    CharBuffer image= ResourceFinder.readDataFileToCharBuffer(fileName);
                    fileDataImage = image;
                }
            }
        }
    }

    @Override
    public String apply(long input) {


        int offset = positionRange.applyAsInt(input);
        int length = sizeRange.applyAsInt(input);
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
