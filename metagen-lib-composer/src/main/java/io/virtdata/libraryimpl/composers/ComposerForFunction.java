package io.virtdata.libraryimpl.composers;

import io.virtdata.api.FunctionType;
import io.virtdata.libraryimpl.FunctionComposer;

import java.util.function.*;

public class ComposerForFunction implements FunctionComposer<Function<?,?>> {

    private final Function<?, ?> inner;

    public ComposerForFunction(Function<?, ?> inner) {
        this.inner = inner;
    }

    @Override
    public Object getFunctionObject() {
        return inner;
    }

    @Override
    @SuppressWarnings("unchecked")
    public FunctionComposer andThen(Object outer) {
        FunctionType functionType = FunctionType.valueOf(outer);
        switch (functionType) {

            case long_long:
                final Function<?, Long> f1 =
                        (Object o) ->
                                ((LongUnaryOperator) outer).applyAsLong(((Function<Object, Long>) inner).apply(o));
                return new ComposerForFunction(f1);
            case long_T:
                final Function<Object,Object> f2 =
                        (Object o) ->
                                ((LongFunction<?>)outer).apply(((Function<Object,Long>)inner).apply(o));
                return new ComposerForFunction(f2);
            case long_int:
                final Function<Object,Integer> f3 =
                        (Object o) ->
                                ((LongToIntFunction)outer).applyAsInt(((Function<Object,Integer>)inner).apply(o));
                return new ComposerForFunction(f3);
            case long_double:
                final Function<Object,Double> f4=
                        (Object o) ->
                                ((LongToDoubleFunction)outer).applyAsDouble(((Function<Object,Long>)inner).apply(o));
                return new ComposerForFunction(f4);
            case R_T:
                final Function<Object,Object> f5=
                        (Object o) ->
                                ((Function<Object,Object>)outer).apply(((Function<Object,Object>)inner).apply(o));
                return new ComposerForFunction(f5);
            case int_int:
                final Function<Object,Integer> f6=
                        (Object o) ->
                                ((IntUnaryOperator)outer).applyAsInt(((Function<Object,Integer>)inner).apply(o));
                return new ComposerForFunction(f6);

            default:
                throw new RuntimeException(functionType + " is not recognized");

        }
    }
}
