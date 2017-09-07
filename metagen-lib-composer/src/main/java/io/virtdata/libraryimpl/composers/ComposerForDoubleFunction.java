package io.virtdata.libraryimpl.composers;

import io.virtdata.api.FunctionType;
import io.virtdata.libraryimpl.FunctionComposer;

import java.util.function.*;

public class ComposerForDoubleFunction implements FunctionComposer<DoubleFunction<?>> {

    private final DoubleFunction<?> inner;

    public ComposerForDoubleFunction(DoubleFunction<?> inner) {
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
                final DoubleToLongFunction f1 =
                        (double d) -> ((LongUnaryOperator) outer).applyAsLong(((DoubleToLongFunction) inner).applyAsLong(d));
                return new ComposerForDoubleToLongFunction(f1);
            case long_T:
                final DoubleFunction<?> f2 =
                        (double d) -> ((LongFunction<?>) outer).apply(((DoubleFunction<Long>) inner).apply(d));
                return new ComposerForDoubleFunction(f2);
            case long_int:
                final DoubleToIntFunction f3 =
                        (double d) -> ((LongToIntFunction) outer).applyAsInt(((DoubleFunction<Long>) inner).apply(d));
                return new ComposerForDoubleToIntFunction(f3);
            case long_double:
                final DoubleUnaryOperator f4 =
                        (double d) -> ((LongToDoubleFunction) outer).applyAsDouble(((DoubleFunction<Long>) inner).apply(d));
                return new ComposerForDoubleUnaryOperator(f4);
            case int_int:
                final DoubleToIntFunction f5 =
                        (double d) -> ((IntUnaryOperator) outer).applyAsInt(((DoubleFunction<Integer>)inner).apply(d));
                return new ComposerForDoubleToIntFunction(f5);
            case int_long:
                final DoubleToLongFunction f6 =
                        (double d) -> ((IntToLongFunction) outer).applyAsLong(((DoubleFunction<Integer>)inner).apply(d));
                return new ComposerForDoubleToLongFunction(f6);
            case int_double:
                final DoubleUnaryOperator f7 =
                        (double d) -> ((IntToDoubleFunction) outer).applyAsDouble(((DoubleFunction<Integer>)inner).apply(d));
                return new ComposerForDoubleUnaryOperator(f7);
            case int_T:
                final DoubleFunction<?> f8 =
                        (double d) -> ((IntFunction<?>) outer).apply(((DoubleFunction<Integer>)inner).apply(d));
                return new ComposerForDoubleFunction(f8);
            case double_double:
                final DoubleUnaryOperator f9 =
                        (double d) -> ((DoubleUnaryOperator)outer).applyAsDouble(((DoubleFunction<Double>)inner).apply(d));
                return new ComposerForDoubleUnaryOperator(f9);
            case double_long:
                final DoubleToLongFunction f10 =
                        (double d) -> ((DoubleToLongFunction)outer).applyAsLong(((DoubleFunction<Long>)inner).apply(d));
                return new ComposerForDoubleToLongFunction(f10);
            case double_int:
                final DoubleToIntFunction f11 =
                        (double d) -> ((DoubleToIntFunction)outer).applyAsInt(((DoubleFunction<Integer>)inner).apply(d));
                return new ComposerForDoubleToIntFunction(f11);
            case double_T:
                final DoubleFunction<?> f12 =
                        (double d) -> ((DoubleFunction<?>)outer).apply(((DoubleFunction<Double>)inner).apply(d));
                return new ComposerForDoubleFunction(f12);
            case R_T:
            default:
                throw new RuntimeException(functionType + " is not recognized");

        }
    }
}
