package io.basics.virtdata.parser;

import io.basics.virtdata.ast.MetagenAST;
import io.basics.virtdata.generated.MetagenLexer;
import io.basics.virtdata.generated.MetagenParser;
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
            MetagenLexer lexer = new MetagenLexer(ais);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MetagenParser parser = new MetagenParser(tokens);
            MetagenBuilder astListener = new MetagenBuilder();
//        MetagenContextBuilder contextBuilder = new MetagenContextBuilder();
            parser.addParseListener(astListener);
//        parser.addParseListener(contextBuilder);

            if (logger.isTraceEnabled()) {
                MetagenParser.MetagenRecipeContext metagenRecipeContext = parser.metagenRecipe();
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
