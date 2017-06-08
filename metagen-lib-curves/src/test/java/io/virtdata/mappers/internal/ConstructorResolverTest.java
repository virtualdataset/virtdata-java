package io.virtdata.mappers.internal;

import io.virtdata.reflection.ConstructorResolver;
import io.virtdata.reflection.DeferredConstructor;
import org.testng.annotations.Test;

import java.util.function.LongUnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class ConstructorResolverTest {

    @Test
    public void testNoArgs() {

        DeferredConstructor<Object> dc =
                ConstructorResolver.resolve(new String[]{ATestClass.class.getTypeName()});

        Object aTestClassInstance = dc.construct();
        assertThat(aTestClassInstance).isNotNull();
        assertThat(aTestClassInstance).isInstanceOf(ATestClass.class);
    }

    @Test
    public void testStringArg() {
        DeferredConstructor<Object> dc =
                ConstructorResolver.resolve(new String[]{ATestClass.class.getTypeName(), "oneString"});
        Object aTestClassInstance = dc.construct();
        assertThat(aTestClassInstance).isNotNull();
        assertThat(aTestClassInstance).isInstanceOf(ATestClass.class);
    }

    @Test
    public void testAssignableParameterSignatures() {
        DeferredConstructor<Object> dc =
                ConstructorResolver.resolve(
                        new String[]{
                                ATestClass.class.getTypeName(),
                                "oneString",
                                "twoString"}
                );
        Object aTestClassInstance = dc.construct();
    }

    @Test
    public void testThreeTypes() {
        DeferredConstructor<Object> dc =
                ConstructorResolver.resolve(
                        new String[]{
                                ATestClass.class.getTypeName(),
                                "2.0", "3.0", "3"
                        }
                );
        Object aTestClassInstance = dc.construct();
        assertThat(aTestClassInstance).isNotNull();
        assertThat(aTestClassInstance).isInstanceOf(ATestClass.class);

    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testUnassignableParameterSignature() {
        DeferredConstructor<Object> dc =
                ConstructorResolver.resolve(
                        new String[]{
                                ATestClass.class.getTypeName(),
                                "foo", "bar", "baz"
                        }
                );
        Object nonExtantInstance = dc.construct();
    }

    public static class ATestClass implements LongUnaryOperator {
        public ATestClass() {
        }

        public ATestClass(String one) {

        }

        public ATestClass(String one, Double two) {
        }

        public ATestClass(String one, String two) {

        }

        public ATestClass(double one, float two, int three) {

        }

        @Override
        public long applyAsLong(long operand) {
            return operand;
        }
    }

}