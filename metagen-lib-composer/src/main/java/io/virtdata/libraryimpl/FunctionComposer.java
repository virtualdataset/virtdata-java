package io.virtdata.libraryimpl;

import io.virtdata.api.DataMapper;
import io.virtdata.api.ThreadSafeMapper;
import io.virtdata.core.DataMapperFunctionMapper;
import io.virtdata.core.ResolvedFunction;

public interface FunctionComposer<T> {

    Object getFunctionObject();

    FunctionComposer andThen(Object outer);

    default ResolvedFunction getResolvedFunction() {
        return new ResolvedFunction(getFunctionObject(),
                getFunctionObject().getClass().getAnnotation(ThreadSafeMapper.class) != null
        );
    }

    default ResolvedFunction getResolvedFunction(boolean isThreadSafe) {
        return new ResolvedFunction(getFunctionObject(), isThreadSafe);
    }

    default <R> DataMapper<R> getDataMapper() {
        return DataMapperFunctionMapper.map(getFunctionObject());
    }

}
