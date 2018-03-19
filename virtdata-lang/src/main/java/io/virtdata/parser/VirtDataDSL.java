package io.virtdata.parser;

import io.virtdata.ast.MetagenAST;
import io.virtdata.ast.MetagenFlow;
import io.virtdata.generated.VirtDataLexer;
import io.virtdata.generated.VirtDataParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class VirtDataDSL {
    private final static Logger logger  = LoggerFactory.getLogger(VirtDataDSL.class);

    public static VirtDataDSL.ParseResult parse(String input) {

        try {
            CodePointCharStream cstream = CharStreams.fromString(input);
            VirtDataLexer lexer = new VirtDataLexer(cstream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            VirtDataParser parser = new VirtDataParser(tokens);
            VirtDataBuilder astListener = new VirtDataBuilder();
            parser.addParseListener(astListener);

            VirtDataParser.VirtdataFlowContext metagenFlowContext = parser.virtdataFlow();
            System.out.println(metagenFlowContext.toStringTree(parser));

            if (astListener.hasErrors()) {
                System.out.println(astListener.getErrorNodes());
            }

            MetagenAST ast = astListener.getModel();
            List<MetagenFlow> flows = ast.getFlows();
            if (flows.size() > 1) {
                throw new RuntimeException("Only one flow expected here.");
            }

            if (astListener.hasErrors()) {
                throw new RuntimeException("Error parsing input '" + input + "'");
            }

            return new ParseResult(flows.get(0));

        } catch (Exception e) {
            logger.warn("Error while parsing flow:" + e.getMessage());
            return new ParseResult(e);
        }
    }

    public static class ParseResult {
        public Throwable throwable;
        public MetagenFlow flow;
        public ParseResult(MetagenFlow flow) {
            this.flow = flow;
        }
        public ParseResult(Throwable throwable) {
            this.throwable = throwable;
        }
    }
}
