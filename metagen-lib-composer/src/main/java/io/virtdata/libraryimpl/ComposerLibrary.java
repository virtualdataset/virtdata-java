package io.virtdata.libraryimpl;

import com.google.auto.service.AutoService;
import io.virtdata.api.Generator;
import io.virtdata.api.GeneratorLibrary;
import io.virtdata.core.AllGenerators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@AutoService(GeneratorLibrary.class)
public class ComposerLibrary implements GeneratorLibrary {

    @Override
    public String getLibraryName() {
        return "composer";
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<Generator<T>> getGenerator(String specline) {
        if (!specline.startsWith("compose ")) {
            return Optional.empty();
        }

        String[] specs = specline.substring("compose ".length()).split(" ");

        List<Object> functions = new LinkedList<>();

        Object composed = null;
        for (String spec : specs) {

            Optional<Generator<T>> optionalGenerator =
                    AllGenerators.get().getGenerator(spec);

            Object generator = optionalGenerator.orElseThrow(() -> new RuntimeException("Unable to find generator for " + spec));

            // because debugging
            functions.add(generator);
        }
        FunctionAssembler assy = new FunctionAssembler();
        for (Object function : functions) {
            assy.andThen(function);
        }
        Generator<T> generator = (Generator<T>) assy.getGenerator();
        return Optional.of(generator);

    }


    @Override
    public List<String> getGeneratorNames() {
        List<String> genNames = new ArrayList<>();
        return new ArrayList<String>() {{
            add("compose");
        }};
    }
}
