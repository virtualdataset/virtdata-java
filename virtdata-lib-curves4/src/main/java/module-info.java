module io.virtdata.lib.curves4 {
    requires essentials;

    requires java.compiler;
    requires io.virtdata.annotations;
    exports io.virtdata.finder;

    provides io.virtdata.services.FunctionFinderService with io.virtdata.finder.LibraryFunctionFinder;

    opens io.virtdata.continuous.common;
    opens io.virtdata.continuous.int_double;
    opens io.virtdata.continuous.long_double;
    opens io.virtdata.discrete.common;
    opens io.virtdata.discrete.int_int;
    opens io.virtdata.discrete.int_long;
    opens io.virtdata.discrete.long_int;
    opens io.virtdata.discrete.long_long;
}