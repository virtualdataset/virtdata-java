package io.virtdata.libraryimpl.composers;

import io.virtdata.api.FunctionType;
import io.virtdata.libraryimpl.FunctionComposer;

import java.util.function.*;

public class ComposerForIntFunction implements FunctionComposer<IntFunction<?>> {

    private final IntFunction<?> inner;

    public ComposerForIntFunction(IntFunction<?> inner) {
        this.inner = inner;
    }

    @Override
    public FunctionComposer<?> andThen(Object outer) {
        FunctionType functionType =FunctionType.valueOf(outer);
        switch (functionType) {

            case long_long:
                final IntToLongFunction f1 =
                        (int i) ->
                                ((LongUnaryOperator)outer).applyAsLong( (((IntFunction<Long>)inner).apply(i)).intValue() );
                return new ComposerForIntToLongFunction(f1);
            case long_T:
                final IntFunction<?> f2 =
                        (int i) ->
                                ((LongFunction<?>)outer).apply(((IntFunction<Long>)inner).apply(i));
                return new ComposerForIntFunction(f2);
            case long_int:
                final IntUnaryOperator f3 =
                        (int i) ->
                                ((LongToIntFunction)outer).applyAsInt(((IntFunction<Long>)inner).apply(i));
                return new ComposerForIntUnaryOperator(f3);
            case long_double:
                final IntToDoubleFunction f4 =
                        (int i) ->
                                ((LongToDoubleFunction)outer).applyAsDouble(((IntFunction<Long>)inner).apply(i));
                return new ComposerForIntToDoubleFunction(f4);
            case int_int:
                final IntUnaryOperator f5 =
                        (int i) ->
                                ((IntUnaryOperator)outer).applyAsInt(((IntFunction<Integer>)inner).apply(i));
                return new ComposerForIntUnaryOperator(f5);
            case R_T:
                final IntFunction<?> f6 =
                        (int i) ->
                                ((IntFunction<?>)outer).apply(((IntFunction<Integer>)inner).apply(i));
                return new ComposerForIntFunction(f6);

            default:
                throw new RuntimeException(functionType + " is not recognized");

        }
    }

    @Override
    public Object getFunctionObject() {
        return inner;
    }

}
