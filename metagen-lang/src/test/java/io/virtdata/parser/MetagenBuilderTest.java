package io.virtdata.parser;

import io.virtdata.ast.*;
import io.virtdata.generated.MetagenCallLexer;
import io.virtdata.generated.MetagenCallParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class MetagenBuilderTest {

    private static char[] readFile(String filename) {
        BufferedReader sr = new BufferedReader(
                new InputStreamReader(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)
                )
        );
        CharBuffer cb = CharBuffer.allocate(1000000);
        try {
            while (sr.read(cb) > 0) {
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cb.flip();
        char[] cbimage = new char[cb.limit()];
        cb.get(cbimage, 0, cb.limit());
        return cbimage;
    }

    @Test
    private void testMetagenSyntax() {
        char[] chars = readFile("test-syntax.metagen");
        ANTLRInputStream ais = new ANTLRInputStream(chars, chars.length);
        String inputString = new String(chars);
        System.out.println("Parsing:\n" + inputString);
        MetagenCallLexer lexer = new MetagenCallLexer(ais);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MetagenCallParser parser = new MetagenCallParser(tokens);
        MetagenBuilder astListener = new MetagenBuilder();
        parser.addParseListener(astListener);

        MetagenCallParser.MetagenRecipeContext metagenRecipeContext = parser.metagenRecipe();
        System.out.println(metagenRecipeContext.toStringTree(parser));

        if (astListener.hasErrors()) {
            System.out.println(astListener.getErrorNodes());
        }

        MetagenAST ast = astListener.getModel();
        assertThat(ast.getFlows().size()).isEqualTo(3);

        List<MetagenFlow> flows = ast.getFlows();
        MetagenFlow flow0 = flows.get(0);

        assertThat(flow0.getExpressions().size()).isEqualTo(2);
        Expression expr00 = flow0.getExpressions().get(0);
        assertThat(expr00.getAssignment().getVariableName()).isEqualTo("joy");
        assertThat(expr00.getCall().getFunctionName()).isEqualTo("full");
        assertThat(expr00.getCall().getInputType()).isNull();
        assertThat(expr00.getCall().getOutputType()).isNull();

        Expression expr01 = flow0.getExpressions().get(1);
        assertThat(expr01.getAssignment().getVariableName()).isEqualTo("zero");
        assertThat(expr01.getCall().getFunctionName()).isEqualTo("one");
        assertThat(expr01.getCall().getArgs().size()).isEqualTo(4);

        ArgType arg0 = expr01.getCall().getArgs().get(0);
        assertThat(arg0).isInstanceOf(FunctionCall.class);
        FunctionCall fcArg = (FunctionCall) arg0;
        assertThat(fcArg.getArgs().size()).isEqualTo(1);

        ArgType arg1 = fcArg.getArgs().get(0);
        assertThat(arg1).isInstanceOf(RefArg.class);
        RefArg refArg = (RefArg) arg1;
        assertThat(refArg.getRefName()).isEqualTo("joy");

        MetagenFlow flow1 = flows.get(1);
        assertThat(flow1.getExpressions().size()).isEqualTo(1);
        Expression expr10 = flow1.getExpressions().get(0);

        MetagenFlow flow2 = flows.get(2);
        assertThat(flow2.getExpressions().size()).isEqualTo(1);
        Expression expr20 = flow2.getExpressions().get(0);

    }


}