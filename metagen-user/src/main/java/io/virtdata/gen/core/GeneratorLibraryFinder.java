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
package io.virtdata.gen.core;

import io.virtdata.api.GeneratorLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Convenient singleton for accessing all loadable GeneratorLibrary instances.
 */
public class GeneratorLibraryFinder {

    private static final Logger logger = LoggerFactory.getLogger(GeneratorLibrary.class);

    private static final Map<String, GeneratorLibrary> libraries = new ConcurrentHashMap<>();

    private GeneratorLibraryFinder() {
    }

    public synchronized static GeneratorLibrary get(String libraryName) {
        Optional<GeneratorLibrary> at = Optional.ofNullable(getLibraries().get(libraryName));
        return at.orElseThrow(
                () -> new RuntimeException("GeneratorLibrary '" + libraryName + "' not found.")
        );
    }

    private synchronized static Map<String, GeneratorLibrary> getLibraries() {
        if (libraries.size()==0) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            logger.debug("loading GeneratorLibraries");
            ServiceLoader<GeneratorLibrary> sl = ServiceLoader.load(GeneratorLibrary.class);
            for (GeneratorLibrary at : sl) {
                if (libraries.get(at.getLibraryName()) != null) {
                    throw new RuntimeException("GeneratorLibrary '" + at.getLibraryName()
                            + "' is already defined.");
                }
                libraries.put(at.getLibraryName(),at);
            }
        }
        logger.info("Loaded GeneratorLibraries:" + libraries.keySet());
        return libraries;
    }

    /**
     * Return list of libraries that have been found by this runtime,
     * in alphabetical order of their type names.
     * @return a list of GeneratorLibrary instances.
     */
    public static List<GeneratorLibrary> getAll() {
        List<GeneratorLibrary> libraries = new ArrayList<>(getLibraries().values());
        libraries.sort((o1, o2) -> o1.getLibraryName().compareTo(o2.getLibraryName()));
        return Collections.unmodifiableList(libraries);
    }
}
