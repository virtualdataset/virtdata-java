package io.virtdata.api.types;

import java.util.function.*;

/**
 * <p>Captures the list of function object types which may be used
 * to implement generators. Library implementations may rely on this
 * for type metadata.</p>
 */
public enum FunctionType {

    long_long(LongUnaryOperator.class),
    long_int(LongToIntFunction.class),
    long_double(LongToDoubleFunction.class),
    long_T(LongFunction.class),
    int_int(IntUnaryOperator.class),
    int_long(IntToLongFunction.class),
    int_double(IntToDoubleFunction.class),
    int_T(IntFunction.class),
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
