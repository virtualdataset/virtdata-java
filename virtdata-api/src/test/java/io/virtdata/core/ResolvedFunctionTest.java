package io.virtdata.core;

import org.testng.annotations.Test;

import java.util.function.LongUnaryOperator;

@Test
public class ResolvedFunctionTest {

    @Test
    public void testToStringWithVarArgs() {
        try {
            TestAdd testAdd = new TestAdd(1, 2, 3);
            Class<?>[] parameterTypes = TestAdd.class.getConstructor(int.class, int[].class).getParameterTypes();
            ResolvedFunction rf = new ResolvedFunction(testAdd, true, parameterTypes, new Object[]{1, 2, 3}, long.class, long.class);
            System.out.println(rf.toString());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    private final static class TestAdd implements LongUnaryOperator {

        private final int a;
        private final int[] b;

        public TestAdd(int a, int... b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public long applyAsLong(long operand) {
            return a + operand;
        }
    }
}