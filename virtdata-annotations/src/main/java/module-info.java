module io.virtdata.annotations {

    requires java.compiler;
    requires com.squareup.javapoet;

    exports io.virtdata.annotations;
    exports io.virtdata.autodoctypes;
    exports io.virtdata.processors;

//    provides javax.annotation.processing.Processor with
//            io.virtdata.processors.FunctionDocInfoProcessor,
//            io.virtdata.processors.ServiceProcessor;

}