package io.virtdata.libraryimpl;

import io.virtdata.api.Generator;
import io.virtdata.core.GeneratorFunctionMapper;
import io.virtdata.core.ResolvedFunction;

public interface FunctionComposer<T> {

    Object getFunctionObject();

    FunctionComposer andThen(Object outer);

    default ResolvedFunction getResolvedFunction() {
        return new ResolvedFunction(getFunctionObject());
    }

    default <R> Generator<R> getGenerator() {
        return GeneratorFunctionMapper.map(getFunctionObject());
    }
}
