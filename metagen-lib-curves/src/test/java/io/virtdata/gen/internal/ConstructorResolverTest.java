package io.virtdata.gen.internal;

import org.testng.annotations.Test;

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
                ConstructorResolver.resolve(new String[]{ATestClass.class.getTypeName(),"oneString"});
        Object aTestClassInstance = dc.construct();
        assertThat(aTestClassInstance).isNotNull();
        assertThat(aTestClassInstance).isInstanceOf(ATestClass.class);
    }

    @Test(expectedExceptions = {RuntimeException.class})
    public void testAmbiguous() {
        DeferredConstructor<Object> dc =
                ConstructorResolver.resolve(new String[]{ATestClass.class.getTypeName(),"oneString","twoString"});
        Object aTestClassInstance = dc.construct();
    }

    @Test
    public void testThreeTypes() {
            DeferredConstructor<Object> dc =
                    ConstructorResolver.resolve(
                            new String[]{ATestClass.class.getTypeName(),"2.0","3.0","3"}
                            );
            Object aTestClassInstance = dc.construct();
            assertThat(aTestClassInstance).isNotNull();
            assertThat(aTestClassInstance).isInstanceOf(ATestClass.class);

    }

    public static class ATestClass {
        public ATestClass() {}

        public ATestClass(String one) {

        }

        public ATestClass(String one, Double two) {
        }

        public ATestClass(String one, String two) {

        }

        public ATestClass(double one, float two, int three) {

        }
    }

}