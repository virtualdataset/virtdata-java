package io.virtdata.libbasics.shared.conversions.from_long;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

public class ToStringTest {

    @Test
    public void testNoArgs() {
        ToString t1 = new ToString();
        assertThat(t1.apply(1L)).isEqualTo("1");
    }

    @Test
    public void testWithLongUnaryOperator() {
        ToString t = new ToString(new LongUnaryOperatorIdentity());
        assertThat(t.apply(2L)).isEqualTo("2");
    }

    @Test
    public void testWithLongFunction() {
        ToString t = new ToString(new LongFuncIdentity());
        assertThat(t.apply(3L)).isEqualTo("3");
    }

    @Test
    public void testWithLongObFunc() {
        ToString t = new ToString(new LongObFunc());
        assertThat(t.apply(4L)).isEqualTo("4");
    }

    private static class LongObFunc implements Function<Long,Object> {

        @Override
        public Object apply(Long aLong) {
            return aLong;
        }
    }
    private static class LongUnaryOperatorIdentity implements LongUnaryOperator {
        @Override
        public long applyAsLong(long operand) {
            return operand;
        }
    }

    private static class LongFuncIdentity implements LongFunction<Long> {
        @Override
        public Long apply(long value) {
            return value;
        }
    }



}
