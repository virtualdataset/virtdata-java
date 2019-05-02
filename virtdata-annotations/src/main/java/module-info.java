module io.virtdata.annotations {

    requires com.squareup.javapoet;
    requires org.slf4j;
    requires java.compiler;

    exports io.virtdata.annotations;
    exports io.virtdata.autodoctypes;
    exports io.virtdata.processors;
    exports io.virtdata.services;

//    provides javax.annotation.processing.Processor with
//            io.virtdata.processors.FunctionDocInfoProcessor,
//            io.virtdata.processors.ServiceProcessor;

}