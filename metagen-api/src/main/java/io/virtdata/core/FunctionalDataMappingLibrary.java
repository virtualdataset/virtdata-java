package io.virtdata.core;

import io.virtdata.api.DataMapperLibrary;
import io.virtdata.api.ValueType;
import io.virtdata.api.specs.SpecData;
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

public abstract class FunctionalDataMappingLibrary implements DataMapperLibrary {
    private static final Logger logger = LoggerFactory.getLogger(FunctionalDataMappingLibrary.class);

    public abstract String getLibraryName();
    public abstract List<Package> getSearchPackages();

    @Override
    public List<ResolvedFunction> resolveFunctions(String specifier) {
        SpecData specData = SpecData.forSpec(specifier);

        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();
        List<Class<?>> classes = Collections.emptyList();

        classes = resolveFunctionClasses(specData.getFuncName());

        for (Class<?> aclass : classes) {
            aclass.getCanonicalName();
            String[] dataMapperArgs = (specData.getFuncAndArgs());
            dataMapperArgs[0] = aclass.getCanonicalName();
            try {
                Optional<Object> mapper = ConstructorResolver.resolveAndConstructOptional(dataMapperArgs);
                mapper.ifPresent(m -> resolvedFunctions.add(new ResolvedFunction(m)));
            } catch (Exception e) {
                logger.error("Error while trying to instantiate:" + Arrays.toString(dataMapperArgs));
                throw (e);
            }
        }
        return resolvedFunctions;
    }

    @Override
    public List<String> getDataMapperNames() {

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
            throw new RuntimeException("Search packages must be designated by data mapping library implementations "
                    + " with getSearchPackages(), and may not be overridden.");
        }
        List<Class<?>> classes = new ArrayList<>();
        for (Package aPackage : getSearchPackages()) {
            String fqcn = aPackage.getName() + "." + name;
            try {
                Class<?> dataMapperClass = Class.forName(fqcn);

                // TODO: HERE: filter by output type
                classes.add(dataMapperClass);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return classes;
    }

    @SuppressWarnings("unchecked")
    private Optional<Class<?>> resolveFunctionClass(String name) {
        Class dataMapperClass = null;
        if (name.contains(".")) {
            throw new RuntimeException("Search packages must be designated by data mapping library implementations "
                    + " with getSearchPackages(), and may not be overridden.");
        }

        for (Package aPackage : getSearchPackages()) {
            String fqcn = aPackage.getName() + "." + name;
            try {
                dataMapperClass = Class.forName(fqcn);
                logger.debug("Initialized mapping function '" + fqcn + "'");
                return Optional.of(dataMapperClass);
            } catch (ClassNotFoundException e) {
                logger.trace("candidate mapping function '" + fqcn + "' not found.");
            }
        }
        logger.debug("Unable to find data mapping class " + name);
        return Optional.empty();

    }

    @Override
    public boolean canParseSpec(String spec) {
        return SpecData.forOptionalSpec(spec).isPresent();
    }

    @Override
    public Optional<ResolvedFunction> resolveFunction(String spec) {
        SpecData specData = SpecData.forSpec(spec);
        List<ResolvedFunction> resolvedFunctions = resolveFunctions(spec);
        Optional<ValueType> resultType = specData.getResultType();
        if (resultType.isPresent() && resolvedFunctions.size() > 1) {
            int prefilter = resolvedFunctions.size();

            List<ResolvedFunction> previousFunctions = new ArrayList<>(resolvedFunctions);
            resolvedFunctions = resolvedFunctions.stream()
                    .filter(rf -> rf.getFunctionType().getReturnValueType() == resultType.get())
                    .collect(Collectors.toList());

            int postfilter = resolvedFunctions.size();
            if (prefilter > 0 && postfilter == 0) {
                String warning = "Before filtering for result type '" + resultType.get() + "', there"
                        + " were " + prefilter + " matching functions:" + previousFunctions.stream()
                        .map(Object::toString).collect(Collectors.joining(","));
                // TODO: Move this to a proper impl, remove default methods
                resolvedFunctions = previousFunctions;
            }

        }
        if (resolvedFunctions.size() == 0) {
            return Optional.empty();
        }
        if (resolvedFunctions.size() > 1) {
            Collections.sort(resolvedFunctions, ResolvedFunction.PREFERRED_TYPE_COMPARATOR);
            return Optional.of(resolvedFunctions.get(0));
        }
        return Optional.of(resolvedFunctions.get(0));
    }

}
