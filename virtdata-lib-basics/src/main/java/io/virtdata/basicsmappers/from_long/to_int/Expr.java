package io.virtdata.basicsmappers.from_long.to_int;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.basicsmappers.MVELExpr;
import io.virtdata.threadstate.ThreadLocalState;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.HashMap;
import java.util.function.LongToIntFunction;

/**
 * Allow for the use of arbitrary expressions according to the
 * [MVEL](http://mvel.documentnode.com/) expression language.
 *
 * Variables that have been set by a Save function are available
 * to be used in this function.
 *
 * The variable name **cycle** is reserved, and is always equal to
 * the current input value.
 */
@ThreadSafeMapper
public class Expr implements LongToIntFunction {

    private final String expr;
    private final Serializable compiledExpr;


    public Expr(String expr) {
        this.expr = expr;
        this.compiledExpr = MVELExpr.compile(long.class, "cycle", expr);
    }

    @Override
    public int applyAsInt(long value) {
        HashMap<String, Object> map = ThreadLocalState.tl_ObjectMap.get();
        map.put("cycle",value);
        int result = MVEL.executeExpression(compiledExpr, map, int.class);
        return result;
    }
}
