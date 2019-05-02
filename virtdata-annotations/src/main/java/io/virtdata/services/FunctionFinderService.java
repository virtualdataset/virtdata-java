package io.virtdata.services;

import io.virtdata.autodoctypes.DocFuncData;

import java.util.List;

/**
 * <em>This module is based on JPMS modules.</em>
 */
public interface FunctionFinderService {

    public static String FUNCTIONS_FILE = "FUNCTION-DATA/functions.txt";

    /**
     * Return a list of known functions by module and fully-qualified package name.
     * For example, "io.virtdata.lib.basics/io.virtdata.basicsmappers.unary_int.Add";
     * The part before the slash is the JPMS module name and the part after is the
     * canonical class name.
     *
     * The result will be a list of functions as discovered in the module content,
     * for <EM>only</EM> the owning module.
     *
     * @return A list of zero or more JPMS-qualifed package names.
     */
    public List<Path> getFunctionPaths();

    /**
     * Return a list of information models which describe a usable VirtData function.
     *
     * @return A list of zero or more JPMS-instantiated DocFuncData models.
     */
    public List<? extends DocFuncData> getFuncDataForModule();

    public static class Path {
        public final String moduleName;
        public final String className;
        public final FunctionFinderService finder;

        public Path(String moduleName, String className, FunctionFinderService finder) {
            this.moduleName = moduleName;
            this.className = className;
            this.finder = finder;
        }

        public DocFuncData instantiateDocs() {
            Class<? extends DocFuncData> docFuncDataClass =
                    Class.forName(finder.getClass().getModule(), className + "AutoDocsInfo")
                            .asSubclass(DocFuncData.class);
            DocFuncData docFuncData = null;
            try {
                docFuncData = docFuncDataClass.getConstructor().newInstance();
            } catch (Exception e) {

                throw new RuntimeException(
                        "Error while instantiating " + moduleName + "/" + className + ": " + e.getMessage(),
                        e
                );
            }
            return docFuncData;
        }

        public String toString() {
            return moduleName + "/" + className;
        }
    }


}
