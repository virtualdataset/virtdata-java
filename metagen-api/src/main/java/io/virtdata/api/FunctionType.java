package io.virtdata.api;

import java.util.function.LongUnaryOperator;
import java.util.function.LongFunction;
import java.util.function.Function;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;

/**
 * <p>Captures the list of function object types which may be used
 * to implement generators. Library implementations may rely on this
 * for type metadata.</p>
 */
public enum FunctionType {

    long_long (LongUnaryOperator.class),
    long_T(LongFunction.class),
    long_int(LongToIntFunction.class),
    long_double(LongToDoubleFunction.class),
    R_T(Function.class);

    private final Class<?> functionClass;

    FunctionType(Class<?> functionClass) {
        this.functionClass = functionClass;
    }

    public static FunctionType valueOf(Object g) {
        for (FunctionType functionType : FunctionType.values()) {
            if (functionType.functionClass.isAssignableFrom(g.getClass())) {
                return functionType;
            }
        }
        throw new RuntimeException("Unable to determine FunctionType for object class:" + g.getClass());
    }
}
