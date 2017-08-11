package io.virtdata.parser;

import io.virtdata.ast.*;
import io.virtdata.generated.MetagenBaseListener;
import io.virtdata.generated.MetagenParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class MetagenBuilder extends MetagenBaseListener {
    private final static Logger logger = LoggerFactory.getLogger(MetagenBuilder.class);

    private MetagenAST model = new MetagenAST();
    private List<ErrorNode> errorNodes = new ArrayList<>();

    private Stack<MetagenParser.MetagenFlowContext> flowContexts = new Stack<>();
    private Stack<MetagenParser.ExpressionContext> expressionContexts = new Stack<>();
    private Stack<MetagenParser.MetagenCallContext> callContexts = new Stack<>();

    private LinkedList<MetagenFlow> flows = new LinkedList<>();
    private Stack<FunctionCall> calls = new Stack<FunctionCall>();

    @Override
    public void enterMetagenRecipe(MetagenParser.MetagenRecipeContext ctx) {
        logger.debug("parsing metagen recipe.");

        flowContexts.clear();
        expressionContexts.clear();
        callContexts.clear();

        flows.clear();
//        calls.push(new FunctionCall("root"));
    }

    @Override
    public void exitMetagenRecipe(MetagenParser.MetagenRecipeContext ctx) {
        logger.debug("parsed metagen recipe.");
    }

    @Override
    public void enterMetagenFlow(MetagenParser.MetagenFlowContext ctx) {
        logger.debug("parsing metagen flow.");
        flowContexts.push(ctx);
        flows.push(new MetagenFlow());
        calls.clear();
    }

    @Override
    public void exitMetagenFlow(MetagenParser.MetagenFlowContext ctx) {
        model.addFlow(flows.pop());

        flowContexts.pop();
    }

    @Override
    public void enterExpression(MetagenParser.ExpressionContext ctx) {
        expressionContexts.push(ctx);
        flows.peek().addExpression(new Expression());
        //logger.debug("parsing metagen expression.");
    }

    @Override
    public void exitLvalue(MetagenParser.LvalueContext ctx) {
        flows.peek().getLastExpression().setAssignment(new Assignment(ctx.ID().getSymbol().getText()));
    }

    @Override
    public void exitExpression(MetagenParser.ExpressionContext ctx) {
        expressionContexts.pop();
    }

    @Override
    public void enterMetagenCall(MetagenParser.MetagenCallContext ctx) {
        callContexts.push(ctx);
        calls.push(new FunctionCall());
    }

    @Override
    public void exitMetagenCall(MetagenParser.MetagenCallContext ctx) {

        FunctionCall topFunctionCall = calls.pop();
        if (calls.empty()) {
            flows.peek().getLastExpression().setCall(topFunctionCall);
        } else {
            calls.peek().addFunctionArg(topFunctionCall);
        }

        callContexts.pop();
    }

    @Override
    public void exitInputType(MetagenParser.InputTypeContext ctx) {
        calls.peek().setInputType(ctx.getText());
    }

    @Override
    public void exitFuncName(MetagenParser.FuncNameContext ctx) {
        calls.peek().setFuncName(ctx.getText());
    }

    @Override
    public void exitOutputType(MetagenParser.OutputTypeContext ctx) {
        calls.peek().setOutputType(ctx.getText());
    }

    @Override
    public void exitRef(MetagenParser.RefContext ctx) {
        calls.peek().addFunctionArg(new RefArg(ctx.ID().getText()));
    }

    @Override
    public void exitIntegerValue(MetagenParser.IntegerValueContext ctx) {
        calls.peek().addFunctionArg(new IntegerArg(Integer.valueOf(ctx.getText())));
    }

    @Override
    public void exitFloatValue(MetagenParser.FloatValueContext ctx) {
        calls.peek().addFunctionArg(new FloatArg(Double.valueOf(ctx.getText())));
    }

    @Override
    public void exitStringValue(MetagenParser.StringValueContext ctx) {
        calls.peek().addFunctionArg(new StringArg(ctx.getText().substring(1, ctx.getText().length() - 1)));
    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {
    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {
        super.visitErrorNode(errorNode);
        errorNodes.add(errorNode);
    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }

    public boolean hasErrors() {
        return (errorNodes.size() > 0);
    }

    public List<ErrorNode> getErrorNodes() {
        return errorNodes;
    }

    public FunctionCall getMetagenCall() {
        return calls.peek().getMetagenCall(0);
    }

    public MetagenAST getModel() {
        return model;
    }
}
