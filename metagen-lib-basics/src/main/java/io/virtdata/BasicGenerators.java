package io.virtdata;

import com.google.auto.service.AutoService;
import io.virtdata.api.Generator;
import io.virtdata.api.GeneratorLibrary;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.core.SpecReader;
import io.virtdata.functional.StaticStringGenerator;
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

@SuppressWarnings({"unchecked", "Duplicates"})
@AutoService(GeneratorLibrary.class)
public class BasicGenerators implements GeneratorLibrary {
    private static final Logger logger = LoggerFactory.getLogger(BasicGenerators.class);

    @Override
    public String getLibraryName() {
        return "basics";
    }

    @Override
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        Optional<Class<?>> functionClass = resolveFunctionClass(SpecReader.first(spec));
        Object[] generatorArgs = SpecReader.afterFirst(spec);

        if (functionClass.isPresent()) {
            try {
                Object genr = ConstructorUtils.invokeConstructor(functionClass.get(), generatorArgs);
                return Optional.of(genr).map(g -> new ResolvedFunction(g, this));
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
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(StaticStringGenerator.class.getPackage().getName()))));

        Set<Class<?>> subTypesOf =
                reflections.getSubTypesOf(Object.class);

        ArrayList<String> collected = subTypesOf.stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toCollection(ArrayList::new));

        return collected;

    }

    @SuppressWarnings("unchecked")
    private Optional<Class<?>> resolveFunctionClass(String className) {
        Class<Generator> generatorClass = null;
        if (!className.contains(".")) {
            className = StaticStringGenerator.class.getPackage().getName() + "." + className;
        }

        try {
            generatorClass = (Class<Generator>) Class.forName(className);
            logger.debug("Initialized class:" + generatorClass.getSimpleName() + " for generator type: " + className);
            return Optional.of(generatorClass);
        } catch (ClassNotFoundException e) {
            logger.debug("Unable to map generator class " + className);
            return Optional.empty();
        }
    }


}
