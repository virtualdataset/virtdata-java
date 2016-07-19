package io.virtdata.libraryimpl;

import io.virtdata.api.FunctionType;
import io.virtdata.api.Generator;
import io.virtdata.core.GeneratorFunctionMapper;
import org.testng.annotations.Test;

import java.util.function.*;

@Test
public class FunctionAssemblerMatrix {

    public void testFullPrimitiveMatrix() {

        for (FunctionType ft1 : FunctionType.values()) {
            if (ft1==FunctionType.R_T || ft1==FunctionType.long_T) {
                continue;
            }
            for (FunctionType ft2 : FunctionType.values()) {
                if (ft2==FunctionType.R_T || ft2==FunctionType.long_T) {
                    continue;
                }
                Object f1 = genFunction(ft1);
                Object f2 = genFunction(ft2);
                System.out.println("testing: ft1:" + ft1 + ", ft2:" +ft2 + ", f1:" + f1 +", f2:" + f2);
                AdvancedAssembler assy = new AdvancedAssembler();

                assy.andThen(f1);
                assy.andThen(f2);

                Object function = assy.getFunction();
                Generator g = GeneratorFunctionMapper.map(function);
                Object o = g.get(1L);

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
            case R_T:
                return new F_R_T();
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
    private static class F_long_T implements LongFunction<String> {
        @Override
        public String apply(long value) {
            return String.valueOf(value);
        }
    }
    private static class F_R_T implements Function<String,String> {
        @Override
        public String apply(String s) {
            return s;
        }
    }



}
