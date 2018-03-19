package io.virtdata.parser;

import io.virtdata.ast.Expression;
import org.testng.annotations.Test;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class LambdaBuilderTest {

    @Test
    public void testLambdaChains() {
        Path path=null;
        try {
            URI uri = ClassLoader.getSystemResource("test-syntax-lambda.metagen").toURI();
            path = Paths.get(uri);
            byte[] bytes = Files.readAllBytes(path);
            LambdasDSL.ParseResult parseResult = LambdasDSL.parse(new String(bytes));

            assertThat(parseResult.flow).isNotNull();

            List<Expression> expressions = parseResult.flow.getExpressions();
            assertThat(expressions).hasSize(4);

            Expression e0 = expressions.get(0);
            assertThat(e0.getCall().getFunctionName()).isEqualTo("Func2");

            Expression e1 = expressions.get(1);
            assertThat(e1.getCall().getFunctionName()).isEqualTo("Func3");

            Expression e2 = expressions.get(2);
            assertThat(e2.getCall().getFunctionName()).isEqualTo("func4");

            Expression e3 = expressions.get(3);
            assertThat(e3.getCall().getFunctionName()).isEqualTo("f5");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}