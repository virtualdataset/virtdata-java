module virtdata.api {
    requires slf4j.api;
    requires org.apache.commons.lang3;
    requires virtdata.lang;
    requires virtdata.annotations;
    requires virtdata.processors;
    requires commons.csv;
    exports io.virtdata.util;
}