package io.virtdata.libraryimpl;

import io.virtdata.api.Generator;
import org.testng.annotations.Test;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

@Test
public class FunctionAssemblerTest {

    @Test
    public void testLongUnary() {
        FunctionAssembler fass = new FunctionAssembler();
        fass.andThen(new IdentityOperator());
        Generator<Long> generator = fass.getGenerator();
        Long aLong = generator.get(5);
        assertThat(aLong).isEqualTo(5);
    }

    @Test
    public void testLongUnaryLongUnary() {
        FunctionAssembler fass = new FunctionAssembler();
        fass.andThen(new IdentityOperator());
        fass.andThen(new IdentityOperator());
        Generator<Long> generator = fass.getGenerator();
        Long aLong = generator.get(5);
        assertThat(aLong).isEqualTo(5);
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void testLongFunction() throws Exception {
        FunctionAssembler fass = new FunctionAssembler();
        fass.andThen(new LongAddFiveFunction());
        Generator<Long> generator = fass.getGenerator();
        Long aLong = generator.get(5);
        assertThat(aLong).isEqualTo(10);

    }

    @Test
    public void testLongFunctionLongFunctionProper() {
        FunctionAssembler fass = new FunctionAssembler();
        fass.andThen(new LongAddFiveFunction());
        fass.andThen(new LongAddFiveFunction());
        Generator<Long> generator = fass.getGenerator();
        Long aLong = generator.get(5);
        assertThat(aLong).isEqualTo(15);
    }

    @Test(expectedExceptions = {ClassCastException.class})
    public void testLongFunctionLongFunctionMistyped() throws Exception {
        FunctionAssembler fass = new FunctionAssembler();
        fass.andThen(new LongAddFiveFunction());
        fass.andThen(new GenericStringCat());
        Generator<String> generator = fass.getGenerator();
        generator.get(5);
    }

    @Test
    public void testAndThenFunction() {
        FunctionAssembler fass = new FunctionAssembler();
        fass.andThen(new GenericLongToString());
        Generator<String> generator = fass.getGenerator();
        String s = generator.get(5);
        assertThat(s).isEqualTo("5");
    }

    @Test
    public void testFunctionFunctionProper() {
        FunctionAssembler fass = new FunctionAssembler();
        fass.andThen(new GenericLongToString());
        fass.andThen(new GenericStringCat());
        Generator<String> generator = fass.getGenerator();
        String s = generator.get(5);
        assertThat(s).isEqualTo("Cat5");
    }

    @Test(expectedExceptions = {ClassCastException.class})
    public void testFunctionFunctionMistyped() {
        FunctionAssembler fass = new FunctionAssembler();
        fass.andThen(new GenericStringCat());
        Generator<String> generator = fass.getGenerator();
        String s = generator.get(5);
    }

    @Test
    public void testLongUnaryLongFunctionFunctionProper() {
        FunctionAssembler fass = new FunctionAssembler();
        fass.andThen(new IdentityOperator());
        fass.andThen(new LongAddFiveFunction());
        fass.andThen(new GenericLongToString());
        Generator<String> generator = fass.getGenerator();
        String s = generator.get(5);
        assertThat(s).isEqualTo("10");

    }

    private static class IdentityOperator implements LongUnaryOperator {
        @Override
        public long applyAsLong(long operand) {
            return operand;
        }
    }

    private static class LongAddFiveFunction implements LongFunction<Long> {
        @Override
        public Long apply(long value) {
            return value + 5;
        }
    }

    private static class GenericLongToString implements Function<Long,String> {
        @Override
        public String apply(Long aLong) {
            return String.valueOf(aLong);
        }
    }

    private static class GenericStringCat implements Function<String,String> {
        @Override
        public String apply(String s) {
            return "Cat" + s;
        }
    }

}