package io.virtdata.libraryimpl;

import io.virtdata.api.FunctionType;

import java.util.LinkedList;
import java.util.List;
import java.util.function.*;

/**
 * In order to support efficient lambdas, through the primitive types where possible, we compose
 * functions here by chaining function type to function type.
 * <p>
 * The types listed in FunctionType are supported across the board.
 */
@SuppressWarnings("unchecked")
public class AdvancedAssembler {
    private Object current = null;
    private List<Object> diagnostic = new LinkedList<Object>();

    public AdvancedAssembler andThen(Object f) {
        diagnostic.add(f);

        FunctionType functionType = FunctionType.valueOf(f);
        if (this.current == null) {
            this.current = f;
        } else {
            this.current = andThen(this.current, f);
        }
        return this;
    }

    private Object andThen(Object f1, Object f2) {

        FunctionType ft1 = FunctionType.valueOf(f1);
        FunctionType ft2 = FunctionType.valueOf(f2);

        switch (ft1) {
            case long_long:
                switch (ft2) {
                    case long_long:
                        return (LongUnaryOperator) (long l) -> ((LongUnaryOperator) f2).applyAsLong(((LongUnaryOperator) f1).applyAsLong(l));
                    case long_T:
                        return (LongFunction<?>) (long l) -> ((LongFunction<?>) f2).apply(((LongUnaryOperator) f1).applyAsLong(l));
                    case long_int:
                        return (LongToIntFunction) (long l) -> ((LongToIntFunction) f2).applyAsInt(((LongUnaryOperator) f1).applyAsLong(l));
                    case long_double:
                        return (LongToDoubleFunction) (long l) -> ((LongToDoubleFunction) f2).applyAsDouble(((LongUnaryOperator) f1).applyAsLong(l));
                    case R_T:
                        return (LongFunction<?>) (long l) -> ((Function<Long, ?>) f2).apply(((LongUnaryOperator) f1).applyAsLong(l));
                    default:
                        throw new RuntimeException("long_long -> ??");
                }
            case long_int:
                switch (ft2) {
                    case long_long:
                        return (LongUnaryOperator) (long l) -> ((LongUnaryOperator) f2).applyAsLong(((LongToIntFunction) f1).applyAsInt(l));
                    case long_T:
                        return (LongFunction<?>) (long l) -> ((LongFunction<?>) f2).apply(((LongToIntFunction) f1).applyAsInt(l));
                    case long_int:
                        return (LongToIntFunction) (long l) -> ((LongToIntFunction) f2).applyAsInt(((LongToIntFunction) f1).applyAsInt(l));
                    case long_double:
                        return (LongToDoubleFunction) (long l) -> ((LongToDoubleFunction) f2).applyAsDouble(((LongToIntFunction) f1).applyAsInt(l));
                    case R_T:
                        return (LongFunction<?>) (long l) -> ((Function<Integer, ?>) f2).apply(((LongToIntFunction) f1).applyAsInt(l));
                    default:
                        throw new RuntimeException("long_int -> ??");

                }
            case long_double:
                switch (ft2) {
                    case long_long:
                        return (LongUnaryOperator) (long l) -> ((LongUnaryOperator) f2).applyAsLong((long) ((LongToDoubleFunction) f1).applyAsDouble(l));
                    case long_T:
                        return (LongFunction<?>) (long l) -> ((LongFunction<?>) f2).apply((long) ((LongToDoubleFunction) f1).applyAsDouble(l));
                    case long_int:
                        return (LongToIntFunction) (long l) -> ((LongToIntFunction) f2).applyAsInt((int) ((LongToDoubleFunction) f1).applyAsDouble(l));
                    case long_double:
                        return (LongToDoubleFunction) (long l) -> ((LongToDoubleFunction) f2).applyAsDouble((long) ((LongToDoubleFunction) f1).applyAsDouble(l));
                    case R_T:
                        return (LongFunction<?>) (long l) -> ((Function<Double, ?>) f2).apply(((LongToDoubleFunction) f1).applyAsDouble(l));
                    default:
                        throw new RuntimeException("long_double -> ??");

                }
            case long_T:
                switch (ft2) {
                    case long_long:
                        return (LongUnaryOperator) (long l) -> ((LongUnaryOperator) f2).applyAsLong(((LongFunction<Long>) f1).apply(l));
                    case long_T:
                        return (LongFunction<?>) (long l) -> ((LongFunction<?>) f2).apply(((LongFunction<Long>) f1).apply(l));
                    case long_int:
                        return (LongToIntFunction) (long l) -> ((LongToIntFunction) f2).applyAsInt(((LongFunction<Integer>) f1).apply(l));
                    case long_double:
                        return (LongToDoubleFunction) (long l) -> ((LongToDoubleFunction) f2).applyAsDouble(((LongFunction<Long>) f1).apply(l));
                    case R_T:
                        return (LongFunction<?>) (long l) -> ((Function<Object, ?>) f2).apply(((LongFunction<Object>) f1).apply(l));
                    default:
                        throw new RuntimeException("long_T -> ??");

                }
            case R_T:
                switch (ft2) {
                    case long_long:
                        return (Function<?, Long>) (Object o) -> ((LongUnaryOperator) f2).applyAsLong(((Function<Object, Long>) f1).apply(o));
                    case long_T:
                        return (Function<?, ?>) (Object o) -> ((LongFunction<Object>) f2).apply(((Function<Object, Long>) f1).apply(o));
                    case long_int:
                        return (Function<?, Integer>) (Object o) -> ((LongToIntFunction) f2).applyAsInt(((Function<Object, Long>) f1).apply(o));
                    case long_double:
                        return (Function<?, Double>) (Object o) -> ((LongToDoubleFunction) f2).applyAsDouble(((Function<Object, Long>) f1).apply(o));
                    case R_T:
                        return (Function<?, ?>) (Object o) -> ((Function<Object, Object>) f2).apply(((Function<Object, Object>) f1).apply(o));
                    default:
                        throw new RuntimeException("R_T -> ??");

                }
        }
        throw new RuntimeException("No switch returned from comprehensive list. Something is very wrongs.");
    }

    public Object getFunction() {
        return current;
    }
}
