package io.virtdata.libbasics.shared.functionadapters;

import io.virtdata.annotations.Categories;
import io.virtdata.annotations.Category;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.api.composers.FunctionAssembly;
import io.virtdata.api.composers.FunctionComposer;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.util.VirtDataFunctions;

import java.util.function.LongFunction;

@Categories(Category.conversion)
@ThreadSafeMapper
public class Flow implements LongFunction<Object> {

    private final LongFunction f;

    public Flow(Object... funcs) {
        FunctionComposer fa = new FunctionAssembly();
        for (Object func : funcs) {
            fa = fa.andThen(func);
        }
        ResolvedFunction rf = fa.getResolvedFunction();
        Object functionObject = rf.getFunctionObject();
        f = VirtDataFunctions.adapt(functionObject,LongFunction.class, Object.class, true);
//        f = LongFunction.class.cast(functionObject);
    }

    @Override
    public Object apply(long value) {
        Object o = f.apply(value);
        return o;
    }
}
