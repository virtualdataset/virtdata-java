package io.virtdata.parser;

import io.virtdata.ast.MetagenAST;
import io.virtdata.generated.MetagenCallLexer;
import io.virtdata.generated.MetagenCallParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

public class MetagenLanguage {

    private final static Logger logger = LoggerFactory.getLogger(MetagenLanguage.class);

    public static MetagenAST parse(String description, Reader input) {

        logger.info("Parsing input '" + description + "'.");
        try {
            ANTLRInputStream ais = new ANTLRInputStream(input);
            MetagenCallLexer lexer = new MetagenCallLexer(ais);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MetagenCallParser parser = new MetagenCallParser(tokens);
            MetagenBuilder astListener = new MetagenBuilder();
//        MetagenContextBuilder contextBuilder = new MetagenContextBuilder();
            parser.addParseListener(astListener);
//        parser.addParseListener(contextBuilder);

            if (logger.isTraceEnabled()) {
                MetagenCallParser.MetagenRecipeContext metagenRecipeContext = parser.metagenRecipe();
                logger.trace("parse tree:\n" + metagenRecipeContext.toStringTree(parser));
            }

            if (astListener.hasErrors()) {
                System.out.println(astListener.getErrorNodes());
            }

            MetagenAST metagenAST = astListener.getModel();
            logger.info("Parsed:\n" + description);
            return metagenAST;

//        List<String> context = contextBuilder.getContext();

        } catch (IOException ioe) {
            logger.error("Error while parsing input '" + description + "'.", ioe);
            throw new RuntimeException(ioe);
        }

    }


}
