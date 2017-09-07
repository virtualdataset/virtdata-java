package io.basics.virtdata.libraryimpl.composers;

import io.basics.virtdata.api.FunctionType;
import io.basics.virtdata.libraryimpl.FunctionComposer;

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
            case long_T:
                final Function<Object,Object> f2 =
                        (Object o) ->
                                ((LongFunction<?>)outer).apply(((Function<Object,Long>)inner).apply(o));
                return new ComposerForFunction(f2);
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
            case int_long:
                final Function<Object,Long> f7 =
                        (Object o) ->
                                ((IntToLongFunction)outer).applyAsLong(((Function<Object,Integer>)inner).apply(o));
                return new ComposerForFunction(f7);
            case int_double:
                final Function<Object,Double> f8 =
                        (Object o) ->
                                ((IntToDoubleFunction)outer).applyAsDouble(((Function<Object,Integer>)inner).apply(o));
                return new ComposerForFunction(f8);
            case int_T:
                final Function<Object,?> f9 =
                        (Object o) ->
                                ((IntFunction<?>)outer).apply(((Function<Object,Integer>)inner).apply(o));
                return new ComposerForFunction(f9);
            case double_double:
                final Function<Object,Double> f10 =
                        (Object o) -> ((DoubleUnaryOperator)outer).applyAsDouble(((Function<Object,Double>)inner).apply(o));
                return new ComposerForFunction(f10);
            case double_int:
                final Function<Object,Integer> f11 =
                        (Object o) -> ((DoubleToIntFunction)outer).applyAsInt(((Function<Object,Double>)inner).apply(o));
                return new ComposerForFunction(f11);
            case double_long:
                final Function<Object,Long> f12 =
                        (Object o) -> ((DoubleToLongFunction)outer).applyAsLong(((Function<Object,Double>)inner).apply(o));
                return new ComposerForFunction(f12);
            case double_T:
                final Function<Object,?> f13 =
                        (Object o) -> ((DoubleFunction<?>)outer).apply(((Function<Object,Double>)inner).apply(o));
                return new ComposerForFunction(f13);
            default:
                throw new RuntimeException(functionType + " is not recognized");

        }
    }
}
