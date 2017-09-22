package io.virtdata.api.specs;

import io.virtdata.api.ValueType;
import org.testng.annotations.Test;

import java.util.regex.Matcher;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecDataTest {

    @Test
    public void testFuncNamePattern() {
        Matcher m1 = SpecData.funcNamePattern.matcher("afuncname");
        assertThat(m1.matches()).isTrue();
        Matcher m2 = SpecData.funcNamePattern.matcher("a2");
        assertThat(m2.matches()).isTrue();
        Matcher m3 = SpecData.funcNamePattern.matcher("a2-._");
        assertThat(m3.matches()).isTrue();
        Matcher m4 = SpecData.funcNamePattern.matcher("a(");
        assertThat(m4.matches()).isFalse();
    }

    @Test
    public void testArgsPattern() {
        Matcher m1 = SpecData.argsPattern.matcher("(one,two,three)");
        assertThat(m1.matches()).isTrue();
        Matcher m2 = SpecData.argsPattern.matcher(":one,two,three:");
        assertThat(m2.matches()).isFalse();
        Matcher m3 = SpecData.argsPattern.matcher("(one, two : three)");
        assertThat(m3.matches()).isTrue();
        Matcher m4 = SpecData.argsPattern.matcher("(one , t wo, t h re e:");
        assertThat(m4.matches()).isFalse();
    }

    @Test
    public void testRTypePattern() {
        Matcher m1 = SpecData.resultTypePattern.matcher("-> woot");
        assertThat(m1.matches()).isTrue();
        Matcher m2 = SpecData.resultTypePattern.matcher("=> woot");
        assertThat(m2.matches()).isFalse();
    }

    @Test
    public void testArgPattern() {
        Matcher m1 = SpecData.argScanPattern.matcher("Foo,Bar)");
        assertThat(m1.find()).isTrue();
        assertThat(m1.group("arg").equals("Foo"));
        assertThat(m1.find()).isTrue();
        assertThat(m1.group("arg").equals("Bar"));
    }

    @Test
    public void testBasicSpec() {
        SpecData specData = SpecData.forSpec("afuncname(arg1,2arg2)");
        assertThat(specData.getArgs().length).isEqualTo(2);
        assertThat(specData.getFuncName()).isEqualTo("afuncname");
        assertThat(specData.getArgs()[0]).isEqualTo("arg1");
        assertThat(specData.getArgs()[1]).isEqualTo("2arg2");
    }

    @Test
    public void testRTypeSpec() {
        SpecData specData = SpecData.forSpec("afuncname(arg1,2arg2) -> long");
        assertThat(specData.getArgs().length).isEqualTo(2);
        assertThat(specData.getFuncName()).isEqualTo("afuncname");
        assertThat(specData.getArgs()[0]).isEqualTo("arg1");
        assertThat(specData.getArgs()[1]).isEqualTo("2arg2");
        assertThat(specData.getResultType()).contains(ValueType.LONG);
    }

    @Test
    public void testEmptyArgs() {
        SpecData specData = SpecData.forSpec("afuncname()");
        assertThat(specData.getArgs().length).isEqualTo(0);
    }

    @Test
    public void TestRtypeSpaces() {
        SpecData specData = SpecData.forSpec("Suffix(0000000000) -> String");
        assertThat(specData.getArgs().length).isEqualTo(1);
        assertThat(specData.getResultType()).contains(ValueType.STRING);
    }

    @Test
    public void TestRtypeNoSpaces() {
        SpecData specData = SpecData.forSpec("Suffix(0000000000)->String");
        assertThat(specData.getArgs().length).isEqualTo(1);
        assertThat(specData.getResultType()).contains(ValueType.STRING);
    }

    @Test
    public void TestInnerCommasInsideSQuote() {
        SpecData specData = SpecData.forSpec("fname('inner,comma')");
        assertThat(specData.getArgs().length).isEqualTo(1);
        assertThat(specData.getArgs()[0]).isEqualTo("inner,comma");
    }

// TODO: Solve this with a real parser
//    @Test
//    public void testNestedQuotedParams() {
//        SpecData specData = SpecData.forSpec("A(b('c(1,2)))");
//        assertThat(specData.getArgs().length).isEqualTo(1);
//        assertThat(specData.getArgs()[0]).isEqualTo("b('c(1,2))");
//    }

}