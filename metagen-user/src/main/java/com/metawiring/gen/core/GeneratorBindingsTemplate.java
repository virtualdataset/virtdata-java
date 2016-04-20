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

package com.metawiring.gen.core;

//

import com.metawiring.gen.metagenapi.Generator;
import com.metawiring.gen.metagenapi.GeneratorLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps a set of parameters on an associated object of type T to generators specs.
 * Allows for easy construction of GeneratorBindings when in the proper thread scope.
 *
 * The user is required to call @{link resolveBindings} when in the scope that the resulting
 * bindings will be used.
 */
public class GeneratorBindingsTemplate<T> {
    private final static Logger logger = LoggerFactory.getLogger(GeneratorBindingsTemplate.class);

    private List<String> bindPointsNames = new ArrayList<String>();
    private List<String> generatorSpecs = new ArrayList<String>();

    private T target;
    private GeneratorLibrary genlib = AllGenerators.get(); // by default

    public GeneratorBindingsTemplate(T target, GeneratorLibrary genlib) {
        this.genlib = genlib;
        this.target = target;
    }

    public void addFieldBinding(String bindPointName, String genSpec) {
        this.bindPointsNames.add(bindPointName);
        this.generatorSpecs.add(genSpec);
    }

    public T getTarget() {
        return target;
    }

    /**
     * Use the generator library and the generator specs to create instances of the generators.
     * If you need thread-aware generation, be sure to call this in the proper thread. Each time this method
     * is called, it creates a new instance.
     */
    public GenBindings resolveBindings() {
        List<Generator<?>> generators = new ArrayList<Generator<?>>();
        for (String generatorSpec : generatorSpecs) {
            Generator<?> generator = genlib.getGenerator(generatorSpec);
            generators.add(generator);
        }
        return new GenBindings(this,generators);
    }

    public String toString() {
        String delim="";
        StringBuilder sb = new StringBuilder(GeneratorBindingsTemplate.class.getSimpleName()).append(":");
        sb.append((target!=null)? "owner:" + target : "owner:?");
        for (int i = 0; i < bindPointsNames.size() - 1; i++) {
            sb.append(delim);
            sb.append("'").append(bindPointsNames.get(i)).append("'");
            sb.append("=>");
            sb.append("\"").append(generatorSpecs.get(i)).append("\"");
            sb.append("=>");
            delim=", ";
        }
        return sb.toString();
    }


}
