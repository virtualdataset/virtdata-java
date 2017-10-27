package io.virtdata.core;

import io.virtdata.api.DataMapper;
import io.virtdata.api.DataMapperLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class AllDataMapperLibraries implements DataMapperLibrary {

    private static AllDataMapperLibraries instance = new AllDataMapperLibraries();
    private List<DataMapperLibrary> libraries = DataMapperLibraryFinder.getAll();
    private final Map<String,DataMapper<?>> threadSafeCache = new HashMap<>();

    private final static Logger logger = LoggerFactory.getLogger(AllDataMapperLibraries.class);

    private AllDataMapperLibraries() {
    }

    public static AllDataMapperLibraries get() {
        return instance;
    }

    @Override
    public String getLibraryName() {
        return "ALL";
    }

    private <T> Optional<DataMapper<T>> getDataMapperUnsynced(String spec) {

        List<ResolvedFunction> resolvedFunctions = resolveFunctions(spec);

        if (resolvedFunctions.size()==0) {
            throw new RuntimeException("Unable to resolve a mapping function for " + spec);
        }

//        if (resolvedFunctions.size()>1) {
//            logger.warn("Found " + resolvedFunctions.size() + " resolved functions for '" + spec +"'. This library " +
//                    "expects there to be exactly 1");
//        }

        Optional<ResolvedFunction> optionallyResolvedFunction = Optional.ofNullable(resolvedFunctions.get(0));
        if (optionallyResolvedFunction.isPresent()) {
            ResolvedFunction resolvedFunction = optionallyResolvedFunction.get();
        }
        Optional<DataMapper<T>> dataMapper = optionallyResolvedFunction
                .map(ResolvedFunction::getFunctionObject)
                .map(DataMapperFunctionMapper::map);

        return dataMapper;
    }

    /**
     * This method modifies the usual logic of finding data mapping functions. This is to allow only libraries which
     * can parse the spec to have a chance to map the function objects internally.
     *
     * @param spec A specifier that describes the type and or parameterization of a new data mapper instance.
     * @param <T> result type of a data mapper
     * @return an optional data mapper instance
     */
    @Override
    public <T> Optional<DataMapper<T>> getDataMapper(String spec) {
        if (!canParseSpec(spec)) {
            throw new RuntimeException("No libraries could parse: " + spec);
        }

        synchronized (this) {
            if (threadSafeCache.containsKey(spec)) {
                DataMapper<T> dataMapper = (DataMapper<T>) threadSafeCache.get(spec);
                if (dataMapper != null) {
                    return Optional.ofNullable(dataMapper);
                } // else known to be not marked as threadsafe
            } else { // dont' know if it is threadsafe or not, so must compute in critical section
                Optional<ResolvedFunction> optionallyResolvedFunction = resolveFunction(spec);
                if (optionallyResolvedFunction.isPresent()) {
                    ResolvedFunction resolvedFunction = optionallyResolvedFunction.get();
                    DataMapper<T> mapper = DataMapperFunctionMapper.map(resolvedFunction.getFunctionObject());
                    if (resolvedFunction.isThreadSafe()) {
                        logger.debug("Function " + spec + " is marked as thread safe. Caching and sharing.");
                        threadSafeCache.put(spec,mapper);
                    } else {
                        logger.debug("Function " + spec + " is not thread safe.");
                        threadSafeCache.put(spec,null);
                    }
                } else {
                    return Optional.empty();
                }
                Optional<DataMapper<Object>> newlyResolved = getDataMapperUnsynced(spec);
            }
        }

        return getDataMapperUnsynced(spec);

//        List<ResolvedFunction> resolvedFunctions = resolveFunctions(spec);
//
//        if (resolvedFunctions.size()==0) {
//            throw new RuntimeException("Unable to resolve a mapping function for " + spec);
//        }
//
//        if (resolvedFunctions.size()>1) {
//            logger.warn("Found " + resolvedFunctions.size() + " resolved functions for '" + spec +"'. This library " +
//                    "expects there to be exactly 1");
//        }
//
//        Optional<ResolvedFunction> optionallyResolvedFunction = Optional.ofNullable(resolvedFunctions.get(0));
//        if (optionallyResolvedFunction.isPresent()) {
//            ResolvedFunction resolvedFunction = optionallyResolvedFunction.get();
//        }
//        Optional<DataMapper<T>> dataMapper = optionallyResolvedFunction
//                .map(ResolvedFunction::getFunctionObject)
//                .map(DataMapperFunctionMapper::map);
//
//        return dataMapper;
//
    }

    /**
     * If any composed libraries can parse the spec, we just return that one.
     * @param spec a data mapping function specifier
     * @return true, if this spec is at least parsable by this library
     */
    @Override
    public boolean canParseSpec(String spec) {
        return libraries.stream().map(gl -> gl.canParseSpec(spec)).anyMatch(l-> l);
    }

    /**
     * This method modifies the usual logic of finding data mappers. This is to allow only libraries which
     * can parse the spec to have a chance to map the function objects internally.
     * @param spec A specifier that describes the type and or parameterization of a new data mapper.
     * @return a list of resolved functions
     */
    @Override
    public List<ResolvedFunction> resolveFunctions(String spec) {
        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();

        int parsingLibs=0;
        for (DataMapperLibrary library : libraries) {
            if (library.canParseSpec(spec)) {
                parsingLibs++;
                List<ResolvedFunction> resolvedFunctions1 = library.resolveFunctions(spec);
                resolvedFunctions.addAll(resolvedFunctions1);
            }
        }
        if (parsingLibs==0) {
            throw new RuntimeException("No library could parse: " + spec);
        }
        return resolvedFunctions;
    }

    public Optional<ResolvedFunction> resolveFunction(String spec) {
        List<ResolvedFunction> resolvedFunctionList = resolveFunctions(spec);
        if (resolvedFunctionList.size() == 0) {
            logger.warn("Unable to find data mapper for spec '" + spec + "' in any libimpl, searched in " + toString());
            return Optional.empty();
        }
        if (resolvedFunctionList.size() > 1) {
            String resolvedNames = resolvedFunctionList.stream()
                    .map(r -> r.getClass().getCanonicalName())
                    .collect(Collectors.joining());
            logger.trace("Found more than one matching data mapper for spec '" + spec + "' : " + resolvedNames);
        }
        return Optional.of(resolvedFunctionList.get(0));
    }

    @Override
    public List<String> getDataMapperNames() {
        List<String> genNames = new ArrayList<>();
        for (DataMapperLibrary library : libraries) {
            List<String> libGenNames = library.getDataMapperNames().stream()
                    .map(genName -> library.getLibraryName() + "::" + genName)
                    .collect(Collectors.toList());
            genNames.addAll(libGenNames);
        }
        genNames.sort(Comparator.naturalOrder());
        return genNames;
    }

    public String toString() {
        return AllDataMapperLibraries.class.getSimpleName() + ":"
                + libraries.stream().map(DataMapperLibrary::getLibraryName).collect(Collectors.joining(",", "[", "]"));
    }

}
