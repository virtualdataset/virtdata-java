module virtdata.processors {
    exports io.virtdata.processors;
    requires java.compiler;
    requires com.squareup.javapoet;
    requires virtdata.annotations;
    provides javax.annotation.processing.Processor with
            io.virtdata.processors.FunctionDocInfoProcessor,
            io.virtdata.processors.ServiceProcessor;
}