package io.virtdata.basicsmappers.unary_int;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.basicsmappers.MVELExpr;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.HashMap;
import java.util.function.IntUnaryOperator;

@ThreadSafeMapper
public class Expr implements IntUnaryOperator {
    private static ThreadLocal<HashMap<String,Object>> tlm = ThreadLocal.withInitial(HashMap::new);

    private final String expr;
    private final Serializable compiledExpr;

    public Expr(String expr) {
        this.expr = expr;
        this.compiledExpr = MVELExpr.compile(int.class, "cycle", expr);
    }

    @Override
    public int applyAsInt(int operand) {
        HashMap<String, Object> map = tlm.get();
        map.put("cycle",operand);
        int result = MVEL.executeExpression(compiledExpr, map, int.class);
        return result;
    }
}
