package io.virtdata.parser;

import io.virtdata.ast.MetagenAST;
import io.virtdata.ast.MetagenFlow;
import io.virtdata.generated.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;
import java.util.Optional;


public class LambdaDSL {

    public static Optional<MetagenFlow> parse(String input) {

        try {
            CodePointCharStream cstream = CharStreams.fromString(input);
            LambdasLexer lexer = new LambdasLexer(cstream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LambdasParser parser = new LambdasParser(tokens);
            LambdasBuilder astListener = new LambdasBuilder();
            parser.addParseListener(astListener);

            LambdasParser.MetagenFlowContext metagenFlowContext = parser.metagenFlow();
            System.out.println(metagenFlowContext.toStringTree(parser));

            if (astListener.hasErrors()) {
                System.out.println(astListener.getErrorNodes());
            }

            MetagenAST ast = astListener.getModel();
            List<MetagenFlow> flows = ast.getFlows();
            if (flows.size() > 1) {
                throw new RuntimeException("Only one flow expected here.");
            }

            return Optional.of(flows.get(0));

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
