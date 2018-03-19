package io.virtdata.core;

import io.virtdata.api.DataMapper;
import io.virtdata.api.VirtDataFunctionLibrary;

import java.util.Map;
import java.util.Optional;

public class VirtData implements VirtDataLibrary {

    private VirtDataFunctionLibrary lib;
    private static VirtDataLibrary instance = new VirtData(VirtDataLibraries.get(),"global");
    private String libname;

    private VirtData(VirtDataFunctionLibrary lib, String libname) {
        this.lib = lib;
        this.libname = libname;
    }

    public static VirtDataLibrary get() {
        return instance;
    }

    /**
     * Return an instance of a VirtDataLibrary that is parameterized with the
     * provided library and library name.
     * @param functionLib The function library to use instead of the auto-loaded ones
     * @param libname The library name as it will appear in logging and diagnostics
     * @return a usable instance of a VirtDataLibrary
     */
    public VirtDataLibrary get(VirtDataFunctionLibrary functionLib, String libname) {
        return new VirtData(functionLib, libname);
    }

    @Override
    public VirtDataFunctionLibrary getFunctionLibrary() {
        return lib;
    }

    @Override
    public String getLibname() {
        return libname;
    }

    public static BindingsTemplate getTemplate(String... namesAndSpecs) {
        return get().getBindingsTemplate(namesAndSpecs);
    }

    public static <T> Optional<DataMapper<T>> getMapper(String flowSpec) {
        return get().getDataMapper(flowSpec);
    }

    public static BindingsTemplate getTemplate(Map<String, String> namedBindings) {
        return get().getBindingsTemplate(namedBindings);
    }

    public static <T> DataMapper<T> getMapper(String s, Class<? extends T> clazz) {
        return get().getDataMapper(s, clazz);
    }
}
