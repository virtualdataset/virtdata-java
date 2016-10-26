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

import io.virtdata.api.Generator;
import io.virtdata.api.GeneratorLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Maps a set of parameters on an associated object of type T to generators specs.
 * Allows for easy construction of GeneratorBindings when in the proper thread scope.
 * <p>
 * The user is required to call @{link resolveBindings} when in the scope that the resulting
 * bindings will be used.
 */
public class BindingsTemplate {
    private final static Logger logger = LoggerFactory.getLogger(BindingsTemplate.class);

    private List<String> bindPointNames = new ArrayList<String>();
    private List<String> generatorSpecs = new ArrayList<String>();

    private GeneratorLibrary genlib = AllGenerators.get(); // by default

    public BindingsTemplate(GeneratorLibrary genlib) {
        this.genlib = genlib;
    }

    public void addFieldBinding(String bindPointName, String genSpec) {
        this.bindPointNames.add(bindPointName);
        this.generatorSpecs.add(genSpec);
    }

    /**
     * Use the generator libimpl and the generator specs to create instances of the generators.
     * If you need thread-aware generation, be sure to call this in the proper thread. Each time this method
     * is called, it creates a new instance.
     * @return A set of bindings that can be used to generate object values later
     */
    public Bindings resolveBindings() {
        List<Generator<?>> generators = new ArrayList<Generator<?>>();
        for (String generatorSpec : generatorSpecs) {
            Optional<Generator<Object>> optGenerator = genlib.getGenerator(generatorSpec);
            if (optGenerator.isPresent()) {
                generators.add(optGenerator.get());
            } else {
                logAvailableGenerators();
                throw new RuntimeException(
                        "generator binding was unsuccessful for "
                                + "lib:" + genlib.getLibraryName()
                                + ", spec:" + generatorSpec
                                + ", see log for known generator names.");
            }
        }
        return new Bindings(this, generators);
    }

    private void logAvailableGenerators() {
        genlib.getGeneratorNames().forEach(gn -> logger.info("GENERATOR " + gn));
    }

    public List<String> getBindPointNames() {
        return this.bindPointNames;
    }

    public List<String> getGeneratorSpecs() {
        return this.generatorSpecs;
    }

    public String toString() {
        String delim = "";
        StringBuilder sb = new StringBuilder(BindingsTemplate.class.getSimpleName()).append(":");
        for (int i = 0; i < bindPointNames.size() - 1; i++) {
            sb.append(delim);
            sb.append("'").append(bindPointNames.get(i)).append("'");
            sb.append("=>");
            sb.append("\"").append(generatorSpecs.get(i)).append("\"");
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
            sb.append("\"").append(generatorSpecs.get(i)).append("\"");
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
