/*
*   Copyright 2015 jshook
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
package io.basics.virtdata.core;

import io.basics.virtdata.api.DataMapperLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Convenient singleton for accessing all loadable DataMapper Library instances.
 */
public class DataMapperLibraryFinder {

    private static final Logger logger = LoggerFactory.getLogger(DataMapperLibrary.class);

    private static final Map<String, DataMapperLibrary> libraries = new ConcurrentHashMap<>();

    private DataMapperLibraryFinder() {
    }

    public synchronized static DataMapperLibrary get(String libraryName) {
        Optional<DataMapperLibrary> at = Optional.ofNullable(getLibraries().get(libraryName));
        return at.orElseThrow(
                () -> new RuntimeException("DataMapperLibrary '" + libraryName + "' not found.")
        );
    }

    private synchronized static Map<String, DataMapperLibrary> getLibraries() {
        if (libraries.size()==0) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            logger.debug("loading DataMapper Libraries");
            ServiceLoader<DataMapperLibrary> sl = ServiceLoader.load(DataMapperLibrary.class);
            for (DataMapperLibrary dataMapperLibrary : sl) {
                if (libraries.get(dataMapperLibrary.getLibraryName()) != null) {
                    throw new RuntimeException("DataMapper Library '" + dataMapperLibrary.getLibraryName()
                            + "' is already defined.");
                }
                libraries.put(dataMapperLibrary.getLibraryName(),dataMapperLibrary);
            }
        }
        logger.info("Loaded DataMapper Libraries:" + libraries.keySet());
        return libraries;
    }

    /**
     * Return list of libraries that have been found by this runtime,
     * in alphabetical order of their type names.
     * @return a list of DataMapperLibrary instances.
     */
    public synchronized static List<DataMapperLibrary> getAll() {
        List<DataMapperLibrary> libraries = new ArrayList<>(getLibraries().values());
        libraries.sort((o1, o2) -> o1.getLibraryName().compareTo(o2.getLibraryName()));
        return Collections.unmodifiableList(libraries);
    }
}
