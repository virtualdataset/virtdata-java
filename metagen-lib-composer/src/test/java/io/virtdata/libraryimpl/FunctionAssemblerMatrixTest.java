package io.virtdata.libraryimpl;

import io.virtdata.api.FunctionType;
import io.virtdata.api.DataMapper;
import io.virtdata.core.DataMapperFunctionMapper;
import org.testng.annotations.Test;

import java.util.function.*;

@Test
public class FunctionAssemblerMatrixTest {

    @Test
    public void testFullPrimitiveMatrix() {

        // TODO: Enable full functional introspection and remove the generic skips below
        for (FunctionType ft1 : FunctionType.values()) {
            if (ft1 == FunctionType.R_T || ft1 == FunctionType.long_T || ft1 == FunctionType.int_T) {
                continue;
            }
            for (FunctionType ft2 : FunctionType.values()) {
                if (ft2 == FunctionType.R_T || ft2 == FunctionType.long_T || ft2 == FunctionType.int_T) {
                    continue;
                }
                Object f1 = genFunction(ft1);
                Object f2 = genFunction(ft2);
                System.out.print("testing: ft1:" + ft1 + ", ft2:" + ft2 + ", f1:" + f1 + ", f2:" + f2);
                FunctionComposer assy = new FunctionAssembly();

                assy = assy.andThen(f1);
                assy = assy.andThen(f2);

                DataMapper g = DataMapperFunctionMapper.map(assy.getResolvedFunction().getFunctionObject());
                Object o = g.get(1L);
                System.out.println(" out:" + o);

            }
        }
    }

    private Object genFunction(FunctionType ftype) {
        switch (ftype) {
            case long_double:
                return new F_long_double();
            case long_int:
                return new F_long_int();
            case long_long:
                return new F_long_long();
            case long_T:
                return new F_long_T();
            case int_int:
                return new F_int_int();
            case R_T:
                return new F_R_T();
            case int_long:
                return new F_int_long();
            case int_double:
                return new F_int_double();
            case int_T:
                return new F_int_T();
            default:
                throw new RuntimeException("unrecognized function type: " + ftype);
        }

    }

    private static class F_long_double implements LongToDoubleFunction {
        @Override
        public double applyAsDouble(long value) {
            return (double) value;
        }
    }

    private static class F_long_int implements LongToIntFunction {
        @Override
        public int applyAsInt(long value) {
            return (int) value;
        }
    }

    private static class F_long_long implements LongUnaryOperator {
        @Override
        public long applyAsLong(long operand) {
            return operand;
        }
    }

    private static class F_long_T implements LongFunction<Long> {
        @Override
        public Long apply(long value) {
            return value;
        }
    }

    private static class F_R_T implements Function<Long, Long> {
        @Override
        public Long apply(Long aLong) {
            return aLong;
        }
    }


    private static class F_int_int implements IntUnaryOperator {
        @Override
        public int applyAsInt(int operand) {
            return operand;
        }
    }

    private static class F_int_long implements IntToLongFunction {
        @Override
        public long applyAsLong(int value) {
            return value;
        }
    }

    private static class F_int_double implements IntToDoubleFunction {
        @Override
        public double applyAsDouble(int value) {
            return value;
        }
    }

    private static class F_int_T implements IntFunction<Long> {
        @Override
        public Long apply(int value) {
            return (long) value;
        }
    }
}
