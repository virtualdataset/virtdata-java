package io.virtdata.basicsmappers.from_long.to_long;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.basicsmappers.MVELExpr;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.HashMap;
import java.util.function.LongUnaryOperator;

@ThreadSafeMapper
public class Expr implements LongUnaryOperator {

    private static ThreadLocal<HashMap<String,Object>> tlm = ThreadLocal.withInitial(HashMap::new);
    //private static ThreadLocal<Serializable> compiled= new ThreadLocal<>();

    private final String expr;
    private final Serializable compiledExpr;

    public Expr(String expr) {
        this.expr = expr;
        this.compiledExpr = MVELExpr.compile(long.class, "cycle", expr);
    }

    @Override
    public long applyAsLong(long operand) {
        HashMap<String, Object> map = tlm.get();
        map.put("cycle",operand);
        long result = MVEL.executeExpression(compiledExpr, map, long.class);
        return result;
    }
}
