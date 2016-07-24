package io.virtdata.libraryimpl.composers;

import io.virtdata.api.FunctionType;
import io.virtdata.libraryimpl.FunctionComposer;

import java.util.function.*;

public class ComposerForIntUnaryOperator implements FunctionComposer<IntUnaryOperator> {

    private IntUnaryOperator inner;

    public ComposerForIntUnaryOperator(IntUnaryOperator inner) {
        this.inner = inner;
    }

    @Override
    public Object getFunctionObject() {
        return inner;
    }

    @Override
    public FunctionComposer<?> andThen(Object outer) {
        FunctionType functionType = FunctionType.valueOf(outer);
        switch (functionType) {
            case long_long:
                final IntToLongFunction f1 =
                        (int i) ->
                                ((LongUnaryOperator) outer).applyAsLong(inner.applyAsInt(i));
                return new ComposerForIntToLongFunction(f1);
            case long_T:
                final IntFunction<?> f2 =
                        (int i) ->
                                ((LongFunction<?>) outer).apply(inner.applyAsInt(i));
                return new ComposerForIntFunction(f2);
            case long_int:
                final IntUnaryOperator f3 =
                        (int i) ->
                                ((LongToIntFunction) outer).applyAsInt(inner.applyAsInt(i));
                return new ComposerForIntUnaryOperator(f3);
            case long_double:
                final IntToDoubleFunction f4 =
                        (int i) ->
                                ((LongToDoubleFunction) outer).applyAsDouble(inner.applyAsInt(i));
                return new ComposerForIntToDoubleFunction(f4);
            case R_T:
                final IntFunction<?> f5 =
                        (int i) ->
                                ((Function<Integer, ?>) outer).apply(inner.applyAsInt(i));
                return new ComposerForIntFunction(f5);
            case int_int:
                final IntUnaryOperator f6 =
                        (int i) ->
                                ((IntUnaryOperator) outer).applyAsInt(inner.applyAsInt(i));
                return new ComposerForIntUnaryOperator(f6);

            case int_long:
                final IntToLongFunction f7 =
                        (int i) ->
                                ((IntToLongFunction) outer).applyAsLong(inner.applyAsInt(i));
                return new ComposerForIntToLongFunction(f7);
            case int_double:
                final IntToDoubleFunction f8 =
                        (int i) ->
                                ((IntToDoubleFunction) outer).applyAsDouble(inner.applyAsInt(i));
                return new ComposerForIntToDoubleFunction(f8);
            case int_T:
                final IntFunction<?> f9 =
                        (int i) ->
                                ((IntFunction<?>)outer).apply(inner.applyAsInt(i));
                return new ComposerForIntFunction(f9);

            default:
                throw new RuntimeException(functionType + " is not recognized");

        }
    }

}
