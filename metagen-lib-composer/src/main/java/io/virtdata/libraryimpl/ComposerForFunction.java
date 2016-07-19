package io.virtdata.libraryimpl;

import io.virtdata.api.FunctionType;

import java.util.function.*;

public class ComposerForFunction implements FunctionComposer<Function<?,?>> {

    private final Function<?,?> inner;

    public ComposerForFunction(Function<?,?> inner) {
        this.inner = inner;
    }

    @Override
    public FunctionComposer andThen(Object outer) {
        FunctionType functionType = FunctionType.valueOf(outer);
        switch (functionType) {

            case long_long:
            case long_T:
            case long_int:
            case long_double:
            case R_T:
            default:
                throw new RuntimeException(functionType + " is not recognized");

        }
    }

    @Override
    public Function<?,?> getComposedFunction() {
        return inner;
    }
}
