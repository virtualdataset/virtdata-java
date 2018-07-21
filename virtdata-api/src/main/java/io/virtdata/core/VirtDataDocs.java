package io.virtdata.core;

import io.virtdata.processors.DocFuncData;
import io.virtdata.processors.FunctionDocInfoProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the top-level API supporting access to the documentation models
 * for all known {@link io.virtdata.annotations.ThreadSafeMapper} and
 * {@link io.virtdata.annotations.PerThreadMapper} instances in the runtime.
 */
public class VirtDataDocs {

    public static List<String> getAllNames() {
        VirtDataFunctionFinder finder = new VirtDataFunctionFinder();
        return finder.getFunctionNames();
    }

    public static List<DocFuncData> getAllDocs() {
        VirtDataFunctionFinder finder = new VirtDataFunctionFinder();
        List<String> functionNames = finder.getFunctionNames();
        List<DocFuncData> docs = new ArrayList<>();
        try {
            for (String n : functionNames) {
                String s = n + FunctionDocInfoProcessor.AUTOSUFFIX;
                Class<?> aClass = Class.forName(s);
                Object o = aClass.newInstance();
                if (DocFuncData.class.isAssignableFrom(o.getClass())) {
                    docs.add(DocFuncData.class.cast(o));
                } else {
                    throw new RuntimeException("class " + o.getClass() + " could not be assigned to " + DocFuncData.class.getSimpleName());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while loading doc models:" + e.toString());
        }
        return docs;

    }
}
