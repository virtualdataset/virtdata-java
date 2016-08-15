package io.virtdata.libraryimpl;

import io.virtdata.api.types.FunctionType;
import io.virtdata.libraryimpl.composers.*;

import java.util.List;
import java.util.function.*;

public class FunctionAssembly implements FunctionComposer {

    private FunctionComposer<?> composer = null;

    @Override
    public Object getFunctionObject() {
        if (composer!=null) {
            return composer.getFunctionObject();
        } else {
            throw new RuntimeException("No function have been passed for assembly.");
        }
    }

    @Override
    public FunctionComposer andThen(Object outer) {
        if (composer!=null) {
            composer = composer.andThen(outer);
        } else {
            composer = andThenInitial(outer);
        }
        return composer;
    }

    private FunctionComposer<?> andThenInitial(Object o) {
        FunctionType functionType = FunctionType.valueOf(o);
        switch (functionType) {
            case long_long:
                return new ComposerForLongUnaryOperator((LongUnaryOperator) o);
            case long_T:
                return new ComposerForLongFunction((LongFunction<?>) o);
            case long_int:
                return new ComposerForLongToIntFunction((LongToIntFunction) o);
            case long_double:
                return new ComposerForLongToDouble((LongToDoubleFunction) o);
            case R_T:
                return new ComposerForFunction((Function<?, ?>) o);
            case int_int:
                return new ComposerForIntUnaryOperator((IntUnaryOperator) o);
            case int_long:
                return new ComposerForIntToLongFunction((IntToLongFunction) o);
            case int_double:
                return new ComposerForIntToDoubleFunction((IntToDoubleFunction) o);
            case int_T:
                return new ComposerForIntFunction((IntFunction<?>) o);
            default:
                throw new RuntimeException("Unrecognized function type:" + functionType);
        }
    }

}
