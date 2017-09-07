package io.virtdata.ast;

import java.util.ArrayList;
import java.util.List;

public class MetagenFlow {
    private List<Expression> expressions = new ArrayList();

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void addExpression(Expression expr) {
        expressions.add(expr);
    }

    public Expression getLastExpression() {
        if (expressions.size()==0) {
            throw new RuntimeException("expressions not initialized, last expression 'undefined'");
        }
        return expressions.get(expressions.size()-1);
    }
}
