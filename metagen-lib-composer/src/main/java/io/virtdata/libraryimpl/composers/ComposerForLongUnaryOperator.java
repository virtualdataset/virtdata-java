package io.virtdata.libraryimpl.composers;

import io.virtdata.api.FunctionType;
import io.virtdata.libraryimpl.FunctionComposer;

import java.util.function.*;

public class ComposerForLongUnaryOperator implements FunctionComposer<LongUnaryOperator> {

    private LongUnaryOperator inner;

    public ComposerForLongUnaryOperator(LongUnaryOperator inner) {
        this.inner = inner;
    }

    @Override
    public Object getFunctionObject() {
        return inner;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FunctionComposer<?> andThen(Object outer) {
        FunctionType functionType = FunctionType.valueOf(outer);
        switch (functionType) {
            case long_long:
                final LongUnaryOperator f1 =
                        (long l) -> ((LongUnaryOperator) outer).applyAsLong(inner.applyAsLong(l));
                return new ComposerForLongUnaryOperator(f1);
            case long_T:
                final LongFunction<?> f2 =
                        (long l) -> ((LongFunction<?>)outer).apply(inner.applyAsLong(l));
                return new ComposerForLongFunction(f2);
            case long_int:
                final LongToIntFunction f3 =
                        (long l) -> ((LongToIntFunction)outer).applyAsInt(inner.applyAsLong(l));
                return new ComposerForLongToIntFunction(f3);
            case long_double:
                final LongToDoubleFunction f4 =
                        (long l) -> ((LongToDoubleFunction)outer).applyAsDouble(inner.applyAsLong(l));
                return new ComposerForLongToDouble(f4);
            case R_T:
                final LongFunction<?> f5 =
                        (long l) -> ((Function<Long,?>)outer).apply(inner.applyAsLong(l));
                return new ComposerForLongFunction(f5);
            case int_int:
                final LongToIntFunction f6 =
                        (long l) -> ((IntUnaryOperator)outer).applyAsInt((int) inner.applyAsLong(l));
                return new ComposerForLongToIntFunction(f6);
            case int_long:
                final LongUnaryOperator f7 =
                        (long l) -> ((IntToLongFunction)outer).applyAsLong((int) inner.applyAsLong(l));
                return new ComposerForLongUnaryOperator(f7);

            case int_double:
                final LongToDoubleFunction f8 =
                        (long l) -> ((IntToDoubleFunction)outer).applyAsDouble((int) inner.applyAsLong(l));
                return new ComposerForLongToDouble(f8);

            case int_T:
                final LongFunction<?> f9 =
                        (long l) ->
                                ((IntFunction<?>)outer).apply((int) inner.applyAsLong(l));
                return new ComposerForLongFunction(f9);

            default:
                throw new RuntimeException(functionType + " is not recognized");

        }
    }

}
