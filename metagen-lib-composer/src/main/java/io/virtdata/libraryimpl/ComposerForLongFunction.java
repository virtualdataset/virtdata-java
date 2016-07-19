package io.virtdata.libraryimpl;

import io.virtdata.api.FunctionType;

import java.util.function.*;

public class ComposerForLongFunction implements FunctionComposer<LongFunction<?>> {

    private final LongFunction<?> inner;

    public ComposerForLongFunction(LongFunction<?> inner) {
        this.inner = inner;
    }

    @Override
    public FunctionComposer andThen(Object outer) {
        FunctionType functionType = FunctionType.valueOf(outer);
        switch (functionType) {

            case long_long:
                final LongUnaryOperator f1 =
                        (long l) -> ((LongUnaryOperator) outer).applyAsLong(((LongFunction<Long>) inner).apply(l));
                return new ComposerForLongUnaryOperator(f1);
            case long_T:
                final LongFunction<?> f2 =
                        (long l) -> ((LongFunction<?>) outer).apply(((LongFunction<Long>) inner).apply(l));
                return new ComposerForLongFunction(f2);
            case long_int:
                final LongToIntFunction f3 =
                        (long l) -> ((LongToIntFunction) outer).applyAsInt(((LongFunction<Long>) inner).apply(l));
                return new ComposerForLongToIntFunction(f3);
            case long_double:
                final LongToDoubleFunction f4 =
                        (long l) -> ((LongToDoubleFunction) outer).applyAsDouble(((LongFunction<Long>) inner).apply(l));
            case R_T:
                final LongFunction<?> f5 =
                        (long l) -> ((Function<Long,?>)outer).apply(((LongFunction<Long>)inner).apply(l));

            default:
                throw new RuntimeException(functionType + " is not recognized");

        }
    }

    @Override
    public LongFunction<?> getComposedFunction() {
        return inner;
    }
}
