module io.virtdata.api {
    requires transitive io.virtdata.lang;

    // auto-modules
    requires org.apache.commons.lang3;
    requires commons.csv;
    requires transitive org.slf4j;
    requires io.virtdata.annotations;

    requires java.compiler;

//    uses javax.annotation.processing.Processor;
//    uses io.virtdata.autodoctypes.DocFuncData;

    exports io.virtdata.util;
    exports io.virtdata.core;
    exports io.virtdata.api;
    exports io.virtdata.templates;

    uses io.virtdata.services.FunctionFinderService;
    uses io.virtdata.services.ModuleDataService;

}