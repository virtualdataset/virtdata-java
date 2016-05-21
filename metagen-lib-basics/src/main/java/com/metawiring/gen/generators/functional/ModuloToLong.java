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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuloToLong implements Generator<Long> {
    private final static Logger logger = LoggerFactory.getLogger(ModuloToLong.class);

    private final long modulo;

    public ModuloToLong(long modulo) {
        this.modulo=modulo;
    }
    public ModuloToLong(String modulo) {
        this(Long.valueOf(modulo));
    }

    @Override
    public Long get(long input) {
        long ret = (input % modulo) & Long.MAX_VALUE;
        return ret;
    }

}