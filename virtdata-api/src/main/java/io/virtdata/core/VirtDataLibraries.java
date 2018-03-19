package io.virtdata.core;

import io.virtdata.api.DataMapper;
import io.virtdata.api.VirtDataFunctionLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class VirtDataLibraries implements VirtDataFunctionLibrary  {
    private final static Logger logger = LoggerFactory.getLogger(VirtDataLibraries.class);

    private static VirtDataLibraries instance = new VirtDataLibraries();
    private final Map<String,DataMapper<?>> threadSafeCache = new HashMap<>();

    private final Map<String,VirtDataFunctionLibrary> libraries= Finder.find();

    public static VirtDataLibraries get() {
        return instance;
    }

    private VirtDataLibraries() {
    }
    @Override

    public String getName() {
        return "ALL";
    }

    @Override
    public List<ResolvedFunction> resolveFunctions(
            Class<?> returnType,
            Class<?> inputType,
            String functionName,
            Object... parameters)
    {
        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();
        for (VirtDataFunctionLibrary library : libraries.values()) {
            List<ResolvedFunction> forlib = library.resolveFunctions(returnType, inputType, functionName, parameters);
            // Written this way to allow for easy debugging and understanding, do not convert to .stream()...
            if (forlib.size()>0) {
                resolvedFunctions.addAll(forlib);
            }
        }
        return resolvedFunctions;
    }

    @Override
    public List<String> getDataMapperNames() {
        return this.libraries.values().stream()
                .flatMap(l -> l.getDataMapperNames().stream()).collect(Collectors.toList());
    }

    private static class Finder {
        public synchronized static Map<String, VirtDataFunctionLibrary> find() {
            Map<String,VirtDataFunctionLibrary> libraries = new HashMap<>();
            if (libraries.size()==0) {
                logger.debug("loading DataMapper Libraries");
                ServiceLoader<VirtDataFunctionLibrary> sl = ServiceLoader.load(VirtDataFunctionLibrary.class);
                Map<String,Integer> dups = new HashMap<>();
                for (VirtDataFunctionLibrary functionLibrary : sl) {
                    logger.debug("Found data mapper library:" +
                            functionLibrary.getClass().getCanonicalName() + ":" +
                            functionLibrary.getName());

                    if (libraries.get(functionLibrary.getName()) != null) {
                        String name = functionLibrary.getName();
                        dups.put(name,dups.getOrDefault(name,0)+1);
                    }

                    libraries.put(functionLibrary.getName(),functionLibrary);
                }
                if (dups.size() > 0) {
                    logger.debug("Java runtime provided duplicates for " +
                            dups.entrySet().stream()
                                    .map(e -> e.getKey()+":"+e.getValue()).collect(Collectors.joining(",")));
                }
            }
            logger.info("Loaded DataMapper Libraries:" + libraries.keySet());
            return libraries;
        }

    }
}
