package io.virtdata.libbasics.shared.conversions.from_double;

import org.junit.Test;

import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringTest {

    @Test
    public void TestNoFunc() {
        ToString f = new ToString();
        assertThat(f.apply(1L)).isEqualTo("1.0");
    }

    @Test
    public void TestDoubleUnaryOp() {
        ToString f = new ToString(new DoubleUnaryOperator() {
            @Override
            public double applyAsDouble(double operand) {
                return operand;
            }
        });
        assertThat(f.apply(2L)).isEqualTo("2.0");
    }

    @Test
    public void TestDoubleFunction() {
        ToString f = new ToString(new DoubleFunction<Double>() {

            @Override
            public Double apply(double value) {
                return value;
            }
        });
        assertThat(f.apply(3L)).isEqualTo("3.0");
    }

    @Test
    public void TestFunctionDoubleDouble() {
        ToString f = new ToString(new Function<Double,Double>() {

            @Override
            public Double apply(Double aDouble) {
                return aDouble;
            }
        });
        assertThat(f.apply(4L)).isEqualTo("4.0");
    }


}