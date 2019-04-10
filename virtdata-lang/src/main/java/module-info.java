module io.virtdata.lang {
    requires org.slf4j;
    requires org.antlr.antlr4.runtime;
    opens io.virtdata.parser;
    exports io.virtdata.parser;
    exports io.virtdata.ast;
}