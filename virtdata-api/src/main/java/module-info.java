module virtdata.api {
//    requires transitive virtdata.annotations;
//    requires transitive virtdata.processors;

    exports io.virtdata.util;
    exports io.virtdata.core;
    exports io.virtdata.api;

    requires virtdata.lang;

    requires slf4j.api;
    requires org.apache.commons.lang3;
    requires commons.csv;
    requires virtdata.annotations;
    requires virtdata.processors;
}