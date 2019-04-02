module virtdata.lang {
    requires antlr4.runtime;
    requires slf4j.api;
    opens io.virtdata.parser;
    exports io.virtdata.parser;
    exports io.virtdata.ast;
}