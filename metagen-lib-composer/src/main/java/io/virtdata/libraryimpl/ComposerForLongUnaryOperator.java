package io.virtdata.libraryimpl;

import io.virtdata.api.FunctionType;

import java.util.function.*;

public class ComposerForLongUnaryOperator implements FunctionComposer<LongUnaryOperator> {

    private LongUnaryOperator inner;

    public ComposerForLongUnaryOperator(LongUnaryOperator inner) {
        this.inner = inner;
    }

    @Override
    public FunctionComposer andThen(Object outer) {
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
            default:
                throw new RuntimeException(functionType + " is not recognized");

        }
    }

    @Override
    public LongUnaryOperator getComposedFunction() {
        return inner;
    }
}
