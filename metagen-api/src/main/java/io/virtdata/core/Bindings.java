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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maps a template with named bind points and specifiers onto a set of data
 * mapping function instances. Allows for streamlined calling of mapper functions
 * as a set.
 */
public class Bindings {
    private final static Logger logger = LoggerFactory.getLogger(Bindings.class);

    private BindingsTemplate template;
    private List<DataMapper<?>> dataMappers = new ArrayList<DataMapper<?>>();
    private ThreadLocal<Map<String, DataMapper<?>>> nameCache;

    public Bindings(BindingsTemplate template, List<DataMapper<?>> dataMappers) {
        this.template = template;
        this.dataMappers = dataMappers;
        nameCache = ThreadLocal.withInitial(() ->
                new HashMap<String, DataMapper<?>>() {{
                    for (int i = 0; i < template.getBindPointNames().size(); i++) {
                        put(template.getBindPointNames().get(i), dataMappers.get(i));
                    }
                }});
    }


    public String toString() {
        return template.toString() + dataMappers;
    }

    /**
     * Get a value from each data mapper in the bindings list
     *
     * @param input The long value which the bound data mappers will use as in input
     * @return An array of objects, the values yielded from each data mapper in the bindings list
     */
    public Object[] getAll(long input) {
        Object[] values = new Object[dataMappers.size()];
        int offset = 0;
        for (DataMapper dataMapper : dataMappers) {
            values[offset++] = dataMapper.get(input);
        }
        return values;
    }

    public BindingsTemplate getTemplate() {
        return this.template;
    }

    /**
     * Get a value for the data mapper in slot i
     *
     * @param i     the data mapper slot, 0-indexed
     * @param input the long input value which the bound data mapper will use as input
     * @return a single object, the value yielded from the indexed data mapper in the bindings list
     */
    public Object get(int i, long input) {
        return dataMappers.get(i).get(input);
    }

    public Object get(String s, long input) {
        DataMapper<?> dataMapper = nameCache.get().get(s);
        return dataMapper.get(input);
    }

    /**
     * Generate all values in the bindings template, and set each of them in
     * the map according to their bind point name.
     *
     * @param donorMap - a user-provided Map&lt;String,Object&gt;
     * @param cycle    - the cycle for which to generate the values
     */
    public void setMap(Map<String, Object> donorMap, long cycle) {
        Object[] all = getAll(cycle);
        for (int i = 0; i < all.length; i++) {
            donorMap.put(template.getBindPointNames().get(i), all[i]);
        }
    }

    /**
     * Generate only the values which have matching keys in the provided
     * map according to their bind point names, and assign them to the
     * map under that name.
     *
     * @param donorMap - a user-provided Map&lt;String,Object&gt;
     * @param cycle    - the cycle for which to generate the values
     */
    public void updateMap(Map<String, Object> donorMap, long cycle) {
        for (String s : donorMap.keySet()) {
            donorMap.put(s, get(s, cycle));
        }
    }

    /**
     * Generate only the values named in fieldNames, and then call the user-provided
     * field setter for each name and object generated.
     *
     * @param fieldSetter user-provided object that implements {@link FieldSetter}.
     * @param cycle       the cycle for which to generate values
     * @param fieldName   A varargs list of field names, or a String[] of names to set
     */
    public void setFields(FieldSetter fieldSetter, long cycle, String... fieldName) {
        for (String s : fieldName) {
            fieldSetter.setField(s,get(s, cycle));
        }
    }

    /**
     * Generate all the values named in the bind point names, then call the user-provided
     * field setter for each name and object generated.
     * @param fieldSetter user-provided object that implements {@link FieldSetter}
     * @param cycle the cycle for which to generate values
     */
    public void setFields(FieldSetter fieldSetter, long cycle) {
        Object[] all = getAll(cycle);
        for (int i = 0; i < all.length; i++) {
            fieldSetter.setField(template.getBindPointNames().get(i), all[i]);
        }
    }

    public LazyValuesMap getLazyMap(long input) {
        return new LazyValuesMap(this, input);
    }

    public static interface FieldSetter {
        void setField(String name, Object value);
    }


}
