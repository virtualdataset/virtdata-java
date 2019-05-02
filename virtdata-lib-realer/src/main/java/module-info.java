module io.virtdata.lib.realer {

    requires io.virtdata.annotations;
    requires io.virtdata.lib.basics;
    requires io.virtdata.lib.realdata;

    exports io.virtdata.librealer;
    opens io.virtdata.librealer;

    provides io.virtdata.services.FunctionFinderService
            with io.virtdata.librealer.LibraryFunctionFinder;

}