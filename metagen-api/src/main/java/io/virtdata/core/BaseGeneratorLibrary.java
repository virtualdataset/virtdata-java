package io.virtdata.core;

import io.virtdata.api.GeneratorLibrary;
import io.virtdata.reflection.ConstructorResolver;
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

public abstract class BaseGeneratorLibrary implements GeneratorLibrary {
    private static final Logger logger = LoggerFactory.getLogger(BaseGeneratorLibrary.class);

    public abstract List<Package> getSearchPackages();

    public abstract String getLibraryName();

    @Override
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        Optional<Class<?>> functionClass = resolveFunctionClass(SpecReader.first(spec));
        String[] generatorArgs = SpecReader.split(spec);

        if (functionClass.isPresent()) {
            generatorArgs[0] = functionClass.get().getCanonicalName();
            try {
                Object genr = ConstructorResolver.resolveAndConstruct(generatorArgs);
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


        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner());
        cb.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])));

        FilterBuilder fb = new FilterBuilder();

        for (Package aPackage : getSearchPackages()) {
            fb.include(FilterBuilder.prefix(aPackage.getName()));
        }
        cb.filterInputsBy(fb);
        Reflections reflections = new Reflections(cb);

        Set<Class<?>> subTypesOf =
                reflections.getSubTypesOf(Object.class);

        ArrayList<String> collected = subTypesOf.stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toCollection(ArrayList::new));

        return collected;

    }

    @SuppressWarnings("unchecked")
    private Optional<Class<?>> resolveFunctionClass(String className) {
        Class generatorClass = null;
        if (className.contains(".")) {
            throw new RuntimeException("Search packages must be designated by generator library.");
        }

        for (Package aPackage : getSearchPackages()) {
            String fqcn = aPackage.getName() + "." + className;
            try {
                generatorClass = Class.forName(fqcn);
                logger.debug("Initialized mapping function '" + fqcn + "'");
                return Optional.of(generatorClass);
            } catch (ClassNotFoundException e) {
                logger.trace("candidate mapping function '" + fqcn + "' not found.");
            }
        }
        logger.debug("Unable to map generator class " + className);
        return Optional.empty();

    }


}
