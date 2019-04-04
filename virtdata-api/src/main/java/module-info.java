import io.virtdata.autodoctypes.DocFuncData;

module virtdata.api {
//    requires transitive virtdata.annotations;
//    requires transitive virtdata.processors;


    exports io.virtdata.util;
    exports io.virtdata.core;
    exports io.virtdata.api;

    requires virtdata.lang;
    requires transitive virtdata.annotations;
    requires virtdata.processors;

    // auto-modules
    requires org.apache.commons.lang3;
    requires commons.csv;
    requires org.slf4j;

    uses DocFuncData;
}