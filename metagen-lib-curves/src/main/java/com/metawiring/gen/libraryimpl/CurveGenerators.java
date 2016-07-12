package com.metawiring.gen.libraryimpl;

import com.google.auto.service.AutoService;
import com.metawiring.gen.generators.DiscreteDistributionSampler;
import com.metawiring.gen.metagenapi.Generator;
import com.metawiring.gen.metagenapi.GeneratorLibrary;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@AutoService(GeneratorLibrary.class)
public class CurveGenerators implements GeneratorLibrary {
    private static final Logger logger = LoggerFactory.getLogger(CurveGenerators.class);

    private static Object[] parseGeneratorArgs(String generatorType) {
        String[] parts = generatorType.split(":");
        return Arrays.copyOfRange(parts, 1, parts.length);
    }

    @Override
    public String getLibraryName() {
        return "basics";
    }

    @Override
    public <T> Optional<Generator<T>> getGenerator(String spec) {
        Optional<Class<Generator>> generatorClass = resolveGeneratorClass(spec);
        Object[] generatorArgs = parseGeneratorArgs(spec);

        if (generatorClass.isPresent()) {
            try {
                Generator generator = ConstructorUtils.invokeConstructor(generatorClass.get(), generatorArgs);
                return Optional.of(generator);
            } catch (Exception e) {
                logger.error("Error instantiating generator:" + e.getMessage(), e);
                return Optional.empty();
            }
        } else {
            logger.debug("Generator class not found: " + spec);
            return Optional.empty();
        }
    }

    @Override
    public List<String> getGeneratorNames() {
        List<String> genNames = new ArrayList<>();


        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(DiscreteDistributionSampler.class.getPackage().getName()))));


        Set<Class<?>> subTypesOf =
                reflections.getSubTypesOf(Object.class)
                ;

        ArrayList<String> collected = subTypesOf.stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toCollection(ArrayList::new));

        return collected;

    }

    @SuppressWarnings("unchecked")
    private Optional<Class<Generator>> resolveGeneratorClass(String generatorSpec) {
        Class<Generator> generatorClass = null;
        String className = (generatorSpec.split(":"))[0];
        if (!className.contains(".")) {
            className = DiscreteDistributionSampler.class.getPackage().getName() + "." + className;
        }

        try {
            generatorClass = (Class<Generator>) Class.forName(className);
            logger.debug("Initialized class:" + generatorClass.getSimpleName() + " for generator type: " + generatorSpec);
            return Optional.of(generatorClass);
        } catch (ClassNotFoundException e) {
            logger.debug("Unable to map generator class " + generatorSpec);
            return Optional.empty();
        }
    }


}
