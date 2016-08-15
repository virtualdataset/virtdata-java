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
    public List<ResolvedFunction> resolveFunctions(String spec) {

        List<Class<?>> classes = resolveFunctionClasses(SpecReader.first(spec));

        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();

        for (Class<?> aclass : classes) {
            aclass.getCanonicalName();
            String[] generatorArgs = SpecReader.split(spec);
            generatorArgs[0] = aclass.getCanonicalName();
            try {
                Object mapper = ConstructorResolver.resolveAndConstruct(generatorArgs);
                resolvedFunctions.add(new ResolvedFunction(mapper));
            } catch (Exception e) {
                logger.error("Error while trying to instantiate:" + Arrays.toString(generatorArgs));
                throw (e);
            }
        }
        return resolvedFunctions;
    }

    @Override
    public List<String> getGeneratorNames() {

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

    private List<Class<?>> resolveFunctionClasses(String name) {
        if (name.contains(".")) {
            throw new RuntimeException("Search packages must be designated by generator lib implementations "
                    + " with getSearchPackages(), and may not be overridden.");
        }
        List<Class<?>> classes = new ArrayList<>();
        for (Package aPackage : getSearchPackages()) {
            String fqcn = aPackage.getName() + "." + name;
            try {
                Class<?> generatorClass = Class.forName(fqcn);
                classes.add(generatorClass);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return classes;
    }

    @SuppressWarnings("unchecked")
    private Optional<Class<?>> resolveFunctionClass(String name) {
        Class generatorClass = null;
        if (name.contains(".")) {
            throw new RuntimeException("Search packages must be designated by generator lib implementations "
                    + " with getSearchPackages(), and may not be overridden.");
        }

        for (Package aPackage : getSearchPackages()) {
            String fqcn = aPackage.getName() + "." + name;
            try {
                generatorClass = Class.forName(fqcn);
                logger.debug("Initialized mapping function '" + fqcn + "'");
                return Optional.of(generatorClass);
            } catch (ClassNotFoundException e) {
                logger.trace("candidate mapping function '" + fqcn + "' not found.");
            }
        }
        logger.debug("Unable to map generator class " + name);
        return Optional.empty();

    }


}
