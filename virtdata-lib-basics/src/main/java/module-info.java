module virtdata.lib.basics {
    requires joda.time;
    requires essentials;
    requires slf4j.api;
    requires number.to.words;
    requires mvel2;
    requires commons.csv;
    requires virtdata.api;

    requires virtdata.annotations;
    requires virtdata.processors;
    requires java.compiler;
    uses javax.annotation.processing.Processor;
}