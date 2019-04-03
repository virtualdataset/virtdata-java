import io.virtdata.processors.types.ExampleDocData;

module virtdata.lib.basics {

    requires joda.time;
    requires essentials;
    requires slf4j.api;
    requires number.to.words;
    requires mvel2;
    requires commons.csv;
    requires virtdata.api;

    requires io.virtdata.annotations;
    requires java.compiler;

    requires virtdata.processors;
    uses javax.annotation.processing.Processor;

    //provides io.virtdata.processors.types.DocFuncData with ExampleDocData;

}