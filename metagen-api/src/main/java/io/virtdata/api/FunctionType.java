package io.virtdata.api;

import java.util.function.LongUnaryOperator;
import java.util.function.LongFunction;
import java.util.function.Function;

/**
 * <p>Captures the list of function object types which may be used
 * to implement generators. Library implementations may rely on this
 * for type metadata.</p>
 * <p>Presently, the types that are allowed for mapping functions:</p>
 * <ul>
 * <li>{@link java.util.function.LongUnaryOperator}</li>
 * <li>{@link java.util.function.LongFunction}</li>
 * <li>{@link java.util.function.Function}</li>
 * </ul>
 * <p>
 * Metagener libraries are required to support these types for mapping function implementations.
 * </p>
 */
public enum FunctionType {

    LongUnaryOperator(LongUnaryOperator.class),
    LongFunction(LongFunction.class),
    Function(Function.class);

    private final Class<?> functionClass;

    FunctionType(Class<?> functionClass) {
        this.functionClass = functionClass;
    }

    public static FunctionType valueOf(Object g) {
        for (FunctionType functionType : FunctionType.values()) {
            if (functionType.functionClass.isAssignableFrom(functionType.functionClass)) {
                return functionType;
            }
        }
        throw new RuntimeException("Unable to determine FunctionType for object class:" + g.getClass());
    }
}
