package io.virtdata.parser;

import io.virtdata.ast.VirtDataAST;
import io.virtdata.generated.VirtDataLexer;
import io.virtdata.generated.VirtDataParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

public class VirtDataLanguage {

    private final static Logger logger = LoggerFactory.getLogger(VirtDataLanguage.class);

    public static VirtDataAST parse(String description, Reader input) {

        logger.info("Parsing input '" + description + "'.");
        try {
            CodePointCharStream charStream = CharStreams.fromReader(input);
            VirtDataLexer lexer = new VirtDataLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            VirtDataParser parser = new VirtDataParser(tokens);
            VirtDataBuilder astListener = new VirtDataBuilder();
            parser.addParseListener(astListener);

            if (logger.isTraceEnabled()) {
                VirtDataParser.VirtdataRecipeContext virtdataRecipeContext = parser.virtdataRecipe();
                logger.trace("parse tree:\n" + virtdataRecipeContext.toStringTree(parser));
            }

            if (astListener.hasErrors()) {
                System.out.println(astListener.getErrorNodes());
            }

            VirtDataAST virtDataAST = astListener.getModel();
            logger.info("Parsed:\n" + description);
            return virtDataAST;

//        List<String> context = contextBuilder.getContext();

        } catch (IOException ioe) {
            logger.error("Error while parsing input '" + description + "'.", ioe);
            throw new RuntimeException(ioe);
        }

    }


}
