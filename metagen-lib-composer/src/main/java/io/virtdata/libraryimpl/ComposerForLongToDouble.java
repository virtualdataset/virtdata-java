package io.virtdata.libraryimpl;

import io.virtdata.api.FunctionType;

import java.util.function.LongFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;

public class ComposerForLongToDouble implements FunctionComposer<LongToDoubleFunction> {
    private final LongToDoubleFunction inner;

    public ComposerForLongToDouble(LongToDoubleFunction inner) {
        this.inner = inner;
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
            default:
                throw new RuntimeException(functionType + " is not recognized");
        }
    }

    @Override
    public LongToDoubleFunction getComposedFunction() {
        return inner;
    }
}
