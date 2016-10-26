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

package io.virtdata.core;

//

import io.virtdata.api.DataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps a template of generator bind points and generator specs onto a set of generator
 * instances. Allows for streamlined calling of generator functions.
 */
public class Bindings {
    private final static Logger logger = LoggerFactory.getLogger(Bindings.class);

    private BindingsTemplate template;
    private List<DataMapper<?>> dataMappers = new ArrayList<DataMapper<?>>();

    public Bindings(BindingsTemplate template, List<DataMapper<?>> dataMappers) {
        this.template = template;
        this.dataMappers = dataMappers;
    }

    public String toString() {
        return template.toString() + dataMappers;
    }

    /**
     * Get a value from each generator in the bindings list
     * @param input The long value which the bound generators will use as in input
     * @return An array of objects, the values generated from each generator in the list
     */
    public Object[] getAll(long input) {
        Object[] values = new Object[dataMappers.size()];
        int offset=0;
        for (DataMapper dataMapper : dataMappers) {
            values[offset++]= dataMapper.get(input);
        }
        return values;
    }

    public BindingsTemplate getTemplate() {
        return this.template;
    }
}
