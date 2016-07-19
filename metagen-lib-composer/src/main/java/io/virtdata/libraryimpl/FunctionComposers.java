package io.virtdata.libraryimpl;

import io.virtdata.api.FunctionType;

import java.util.function.*;

public class FunctionComposers {

    public static FunctionComposer<?> composerFor(Object o) {
        FunctionType functionType = FunctionType.valueOf(o);
        switch (functionType) {
            case long_long:
                return new ComposerForLongUnaryOperator((LongUnaryOperator) o);
            case long_T:
                return new ComposerForLongFunction((LongFunction<?>) o);
            case long_int:
                return new ComposerForLongToIntFunction((LongToIntFunction)o);
            case long_double:
                return new ComposerForLongToDouble((LongToDoubleFunction)o);
            case R_T:
                return new ComposerForFunction((Function<?,?>)o);
            default:
                throw new RuntimeException("Unrecognized function type:" +functionType);
        }
    }
}
