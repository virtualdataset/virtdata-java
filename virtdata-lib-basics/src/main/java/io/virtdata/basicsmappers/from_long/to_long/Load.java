package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.Example;
import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.threadstate.ThreadLocalState;

import java.util.function.LongFunction;
import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class Load implements LongUnaryOperator {

    private final String name;
    private final LongFunction<String> nameGen;

    @Example({"Load('foo')","for the current thread, load a long value from the named variable"})
    public Load(String name) {
        this.name = name;
        this.nameGen=null;
    }

    @Example({"Load(NumberNameToString())","for the current thread, load a long value from the named variable, where the variable name is provided by the provided by a function"})
    public Load(LongFunction<String> nameFunc) {
        this.name=null;
        this.nameGen=nameFunc;
    }
    @Override
    public long applyAsLong(long operand) {
        String varname=(nameGen!=null) ? nameGen.apply(operand) : name;
        Object o = ThreadLocalState.tl_ObjectMap.get().get(varname);
        return (long) o;
    }
}
