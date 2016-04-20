package com.metawiring.gen.metagenapi;

public interface GeneratorLibrary {
    public String getLibraryName();
    public <T> Generator<T> getGenerator(String spec);

}
