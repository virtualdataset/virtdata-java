package io.virtdata.libraryimpl.composers;

import io.virtdata.api.types.FunctionType;
import io.virtdata.libraryimpl.FunctionComposer;

import java.util.function.*;

public class ComposerForLongToDouble implements FunctionComposer<LongToDoubleFunction> {
    private final LongToDoubleFunction inner;

    public ComposerForLongToDouble(LongToDoubleFunction inner) {
        this.inner = inner;
    }

    @Override
    public Object getFunctionObject() {
        return inner;
    }

    @Override
    public FunctionComposer andThen(Object outer) {
        FunctionType functionType = FunctionType.valueOf(outer);
        switch (functionType) {
            case long_long:
                final LongUnaryOperator f1 =
                        (long l) -> ((LongUnaryOperator) outer).applyAsLong((long) inner.applyAsDouble(l));
                return new ComposerForLongUnaryOperator(f1);
            case long_T:
                final LongFunction<?> f2 =
                        (long l) -> ((LongFunction<?>) outer).apply((long) inner.applyAsDouble(l));
                return new ComposerForLongFunction(f2);
            case long_int:
                final LongToIntFunction f3 =
                        (long l) -> ((LongToIntFunction) outer).applyAsInt((int) inner.applyAsDouble(l));
                return new ComposerForLongToIntFunction(f3);
            case long_double:
                final LongToDoubleFunction f4 =
                        (long l) -> ((LongToDoubleFunction) outer).applyAsDouble((long) inner.applyAsDouble(l));
                return new ComposerForLongToDouble(f4);
            case R_T:
                final LongFunction<?> f5 =
                        (long l) -> ((LongFunction<?>) outer).apply((long) inner.applyAsDouble(l));
                return new ComposerForLongFunction(f5);
            case int_int:
                final LongToIntFunction f6 =
                        (long l) ->
                                ((IntUnaryOperator) outer).applyAsInt((int) inner.applyAsDouble(l));
                return new ComposerForLongToIntFunction(f6);
            case int_long:
                final LongUnaryOperator f7 =
                        (long l) -> ((IntToLongFunction) outer).applyAsLong((int) inner.applyAsDouble(l));
                return new ComposerForLongUnaryOperator(f7);

            case int_double:
                final LongToDoubleFunction f8 =
                        (long l) ->
                                ((IntToDoubleFunction) outer).applyAsDouble((int) inner.applyAsDouble(l));
                return new ComposerForLongToDouble(f8);
            case int_T:
                final LongFunction<?> f9 =
                        (long l) ->
                                ((IntFunction<?>) outer).apply((int) inner.applyAsDouble(l));
                return new ComposerForLongFunction(f9);

            default:
                throw new RuntimeException(functionType + " is not recognized");
        }
    }

}
