package io.basics.virtdata.libraryimpl.composers;

import io.basics.virtdata.api.ValueType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class MultiSpecDataTest {

    @Test
    public void testBasicMultiSpec() {
        MultiSpecData msd = MultiSpecData.forSpec("foobarbaz ", "foobarbaz func1(arg1,arg2) ; func2(arg1,arg2)");
        assertThat(msd.getResultType()).isEmpty();
        assertThat(msd.getSpecs().size()).isEqualTo(2);
    }

    @Test
    public void testAllowedSpaceBetweenArgs() {
        MultiSpecData msd = MultiSpecData.forSpec("foobarbaz ", "foobarbaz func1(arg1, arg2) ; func2(arg1, arg2)");
        assertThat(msd.getResultType()).isEmpty();
        assertThat(msd.getSpecs().size()).isEqualTo(2);
        assertThat(msd.getSpecs().get(0).getArgs()[0]).isEqualTo("arg1");
        assertThat(msd.getSpecs().get(0).getArgs()[1]).isEqualTo("arg2");
    }

    @Test
    public void testReportedError() {
        MultiSpecData msd = MultiSpecData.forSpec("compose ", "compose RandomToByteBuffer(1048576) ; ToString()");
        assertThat(msd.getResultType()).isEmpty();
        assertThat(msd.getSpecs().get(0).getFuncName()).isEqualTo("RandomToByteBuffer");
        assertThat(msd.getSpecs().get(0).getArgs()[0]).isEqualTo("1048576");
        assertThat(msd.getSpecs().get(1).getFuncName()).isEqualTo("ToString");

    }

    @Test
    public void testInlineConstraintMultiSpec() {
        MultiSpecData msd = MultiSpecData.forSpec("woot:", "woot:func1(arg1,arg2) -> long ; func2(arg1,arg2)");
        assertThat(msd.getResultType()).isEmpty();
        assertThat(msd.getSpecs().size()).isEqualTo(2);
        assertThat(msd.getSpecs().get(0).getResultType()).contains(ValueType.LONG);
    }

    @Test
    public void testResultConstraintMultiSpec() {
        MultiSpecData msd = MultiSpecData.forSpec("frogteeth ","frogteeth func1(arg1,arg2); func2(arg1,arg2) -> long");
        assertThat(msd.getResultType()).isPresent();
        assertThat(msd.getResultType()).contains(ValueType.LONG);
        assertThat(msd.getSpecs().size()).isEqualTo(2);
        assertThat(msd.getSpecs().get(0).getResultType()).isEmpty();
        assertThat(msd.getSpecs().get(1).getResultType()).contains(ValueType.LONG);
    }

    @Test
    public void TestStrangeChars() {
        MultiSpecData msd = MultiSpecData.forSpec("compose ", "compose Mod(3) ; Suffix('0000000000') -> String");
        assertThat(msd.getResultType()).isPresent();
        assertThat(msd.getResultType()).contains(ValueType.STRING);
    }


}