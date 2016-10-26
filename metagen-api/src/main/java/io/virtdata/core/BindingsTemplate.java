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
import io.virtdata.api.DataMapperLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Maps a set of parameters on an associated object of type T to specifiers for data mappers.
 * Allows for easy construction of DataMapperBindings when in the proper thread scope.
 * <p>
 * The user is required to call @{link resolveBindings} when in the scope that the resulting
 * bindings will be used.
 */
public class BindingsTemplate {
    private final static Logger logger = LoggerFactory.getLogger(BindingsTemplate.class);

    private List<String> bindPointNames = new ArrayList<String>();
    private List<String> specifiers = new ArrayList<String>();

    private DataMapperLibrary genlib = AllDataMapperLibraries.get(); // by default

    public BindingsTemplate(DataMapperLibrary genlib) {
        this.genlib = genlib;
    }

    public void addFieldBinding(String bindPointName, String genSpec) {
        this.bindPointNames.add(bindPointName);
        this.specifiers.add(genSpec);
    }

    /**
     * Use the data mapping library and the specifier to create instances of data mapping functions.
     * If you need thread-aware mapping, be sure to call this in the proper thread. Each time this method
     * is called, it creates a new instance.
     * @return A set of bindings that can be used to yield mapped data values later.
     */
    public Bindings resolveBindings() {
        List<DataMapper<?>> dataMappers = new ArrayList<DataMapper<?>>();
        for (String specifier : specifiers) {
            Optional<DataMapper<Object>> optionalDataMapper = genlib.getDataMapper(specifier);
            if (optionalDataMapper.isPresent()) {
                dataMappers.add(optionalDataMapper.get());
            } else {
                logAvailableDataMappers();
                throw new RuntimeException(
                        "data mapper binding was unsuccessful for "
                                + "lib:" + genlib.getLibraryName()
                                + ", spec:" + specifier
                                + ", see log for known data mapper names.");
            }
        }
        return new Bindings(this, dataMappers);
    }

    private void logAvailableDataMappers() {
        genlib.getDataMapperNames().forEach(gn -> logger.info("DATAMAPPER " + gn));
    }

    public List<String> getBindPointNames() {
        return this.bindPointNames;
    }

    public List<String> getDataMapperSpecs() {
        return this.specifiers;
    }

    public String toString() {
        String delim = "";
        StringBuilder sb = new StringBuilder(BindingsTemplate.class.getSimpleName()).append(":");
        for (int i = 0; i < bindPointNames.size() - 1; i++) {
            sb.append(delim);
            sb.append("'").append(bindPointNames.get(i)).append("'");
            sb.append("=>");
            sb.append("\"").append(specifiers.get(i)).append("\"");
            delim = ", ";
        }
        return sb.toString();
    }

    public String toString(Object[] values) {
        String delim = "";
        StringBuilder sb = new StringBuilder(BindingsTemplate.class.getSimpleName()).append(":");
        for (int i = 0; i < bindPointNames.size() - 1; i++) {
            sb.append(delim);
            sb.append("'").append(bindPointNames.get(i)).append("'");
            sb.append("=>");
            sb.append("\"").append(specifiers.get(i)).append("\"");
            sb.append("=>[");
            sb.append(values[i]);
            sb.append("](");
            sb.append((null!=values[i]) ? values[i].getClass().getSimpleName() : "NULL");
            sb.append(")");
            delim = ", ";
        }
        return sb.toString();

    }


}
