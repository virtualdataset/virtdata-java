package io.virtdata.parser;

import io.virtdata.ast.MetagenAST;
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

    public static MetagenAST parse(String description, Reader input) {

        logger.info("Parsing input '" + description + "'.");
        try {
            CodePointCharStream charStream = CharStreams.fromReader(input);
            VirtDataLexer lexer = new VirtDataLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            VirtDataParser parser = new VirtDataParser(tokens);
            VirtDataBuilder astListener = new VirtDataBuilder();
//        MetagenContextBuilder contextBuilder = new MetagenContextBuilder();
            parser.addParseListener(astListener);
//        parser.addParseListener(contextBuilder);

            if (logger.isTraceEnabled()) {
                VirtDataParser.VirtdataRecipeContext metagenRecipeContext = parser.virtdataRecipe();
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
