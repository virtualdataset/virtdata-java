package com.metawiring.gen.libraryimpl;

import com.google.auto.service.AutoService;
import com.metawiring.gen.metagenapi.Generator;
import com.metawiring.gen.metagenapi.GeneratorLibrary;

@AutoService(GeneratorLibrary.class)
public class BasicGenerators implements GeneratorLibrary {

    @Override
    public String getLibraryName() {
        return "basics";
    }

    @Override
    public <T> Generator<T> getGenerator(String spec) {
        return null;
    }

}
