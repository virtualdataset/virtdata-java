package io.virtdata.libraryimpl.composers;

import io.virtdata.api.ValueType;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class MultiSpecDataTest {

    @Test
    public void testBasicMultiSpec() {
        MultiSpecData msd = MultiSpecData.forSpec("foobarbaz ", "foobarbaz func1(arg1,arg2) ; func2(arg1,arg2)");
        Assertions.assertThat(msd.getResultType()).isEmpty();
        assertThat(msd.getSpecs().size()).isEqualTo(2);
    }

    @Test
    public void testAllowedSpaceBetweenArgs() {
        MultiSpecData msd = MultiSpecData.forSpec("foobarbaz ", "foobarbaz func1(arg1, arg2) ; func2(arg1, arg2)");
        Assertions.assertThat(msd.getResultType()).isEmpty();
        assertThat(msd.getSpecs().size()).isEqualTo(2);
        Assertions.assertThat(msd.getSpecs().get(0).getArgs()[0]).isEqualTo("arg1");
        Assertions.assertThat(msd.getSpecs().get(0).getArgs()[1]).isEqualTo("arg2");
    }

    @Test
    public void testReportedError() {
        MultiSpecData msd = MultiSpecData.forSpec("compose ", "compose RandomToByteBuffer(1048576) ; ToString()");
        Assertions.assertThat(msd.getResultType()).isEmpty();
        Assertions.assertThat(msd.getSpecs().get(0).getFuncName()).isEqualTo("RandomToByteBuffer");
        Assertions.assertThat(msd.getSpecs().get(0).getArgs()[0]).isEqualTo("1048576");
        Assertions.assertThat(msd.getSpecs().get(1).getFuncName()).isEqualTo("ToString");

    }

    @Test
    public void testInlineConstraintMultiSpec() {
        MultiSpecData msd = MultiSpecData.forSpec("woot:", "woot:func1(arg1,arg2) -> long ; func2(arg1,arg2)");
        Assertions.assertThat(msd.getResultType()).isEmpty();
        assertThat(msd.getSpecs().size()).isEqualTo(2);
        Assertions.assertThat(msd.getSpecs().get(0).getResultType()).contains(ValueType.LONG);
    }

    @Test
    public void testResultConstraintMultiSpec() {
        MultiSpecData msd = MultiSpecData.forSpec("frogteeth ","frogteeth func1(arg1,arg2); func2(arg1,arg2) -> long");
        Assertions.assertThat(msd.getResultType()).isPresent();
        Assertions.assertThat(msd.getResultType()).contains(ValueType.LONG);
        assertThat(msd.getSpecs().size()).isEqualTo(2);
        Assertions.assertThat(msd.getSpecs().get(0).getResultType()).isEmpty();
        Assertions.assertThat(msd.getSpecs().get(1).getResultType()).contains(ValueType.LONG);
    }

    @Test
    public void TestStrangeChars() {
        MultiSpecData msd = MultiSpecData.forSpec("compose ", "compose Mod(3) ; Suffix('0000000000') -> String");
        Assertions.assertThat(msd.getResultType()).isPresent();
        Assertions.assertThat(msd.getResultType()).contains(ValueType.STRING);
    }


}