import io.virtdata.processors.types.ExampleDocData;

module virtdata.lib.basics {

    requires essentials;
    requires number.to.words;
    requires mvel2;
    requires commons.csv;
    requires virtdata.api;

    requires java.compiler;
    requires virtdata.annotations;
    requires virtdata.processors;
    requires org.joda.time;
//    uses javax.annotation.processing.Processor;
//    uses io.virtdata.processors.FunctionDocInfoProcessor;



    //provides io.virtdata.processors.types.DocFuncData with ExampleDocData;

}