package io.basics.virtdata.libraryimpl;

import io.basics.virtdata.api.DataMapper;
import io.basics.virtdata.api.ThreadSafeMapper;
import io.basics.virtdata.core.DataMapperFunctionMapper;
import io.basics.virtdata.core.ResolvedFunction;

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
