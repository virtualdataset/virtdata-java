package io.virtdata.api;

import java.util.List;
import java.util.Optional;

public interface GeneratorLibrary {

    /**
     * Return the library name for this generator library, as it can be used in spec strings, etc.
     * @return Simple lower-case canonical library name
     */
    String getLibraryName();

    /**
     * Find the implementation for and construct an instance of a generator, as described.
     * @param spec A specifier that describes the type and or parameterization of a new generator.
     * @return An optional generator instances.
     */
    <T> Optional<Generator<T>> getGenerator(String spec);

    /**
     * Get the list of known generator names.
     * @return list of generator names that can be used in generator specs
     */
    List<String> getGeneratorNames();

}
