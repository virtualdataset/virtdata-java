package io.virtdata.core;

import io.virtdata.api.DataMapper;
import io.virtdata.api.VirtDataFunctionLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtDataLibraries implements VirtDataFunctionLibrary  {
    private final static Logger logger = LoggerFactory.getLogger(VirtDataLibraries.class);

    private static VirtDataLibraries instance = new VirtDataLibraries();
    private final Map<String,DataMapper<?>> threadSafeCache = new HashMap<>();

    private final VirtDataFunctionResolver resolver = new VirtDataFunctionResolver();

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


        List<ResolvedFunction> resolved = resolver.resolveFunctions(returnType, inputType, functionName, parameters);
        // Written this way to allow for easy debugging and understanding, do not convert to .stream()...
        if (resolved.size()>0) {
            resolvedFunctions.addAll(resolved);
        }
        return resolvedFunctions;
    }
}
