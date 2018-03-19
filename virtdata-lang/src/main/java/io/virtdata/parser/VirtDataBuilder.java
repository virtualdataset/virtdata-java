package io.virtdata.parser;

import io.virtdata.ast.*;
import io.virtdata.generated.VirtDataBaseListener;
import io.virtdata.generated.VirtDataParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class VirtDataBuilder extends VirtDataBaseListener {
    private final static Logger logger = LoggerFactory.getLogger(VirtDataBuilder.class);

    private MetagenAST model = new MetagenAST();
    private List<ErrorNode> errorNodes = new ArrayList<>();

    private Stack<VirtDataParser.VirtdataFlowContext> flowContexts = new Stack<>();
    private Stack<VirtDataParser.ExpressionContext> expressionContexts = new Stack<>();
    private Stack<VirtDataParser.VirtdataCallContext> callContexts = new Stack<>();

    private LinkedList<MetagenFlow> flows = new LinkedList<>();
    private Stack<FunctionCall> calls = new Stack<FunctionCall>();


    @Override
    public void enterVirtdataRecipe(VirtDataParser.VirtdataRecipeContext ctx) {
        logger.debug("parsing virtdata lambda recipe.");
        flowContexts.clear();
        expressionContexts.clear();
        callContexts.clear();

        flows.clear();
    }

    @Override
    public void exitVirtdataRecipe(VirtDataParser.VirtdataRecipeContext ctx) {
        logger.debug("parsed virtdata recipe.");
    }

    @Override
    public void enterVirtdataFlow(VirtDataParser.VirtdataFlowContext ctx) {
        logger.debug("parsing virtdata flow...");
        flowContexts.push(ctx);
        flows.push(new MetagenFlow());
        calls.clear();
        if (ctx.COMPOSE()!=null) {
            logger.warn("The 'compose' keyword is no longer needed in lambda construction. It will be deprecated in the future.");
        }
    }

    @Override
    public void exitVirtdataFlow(VirtDataParser.VirtdataFlowContext ctx) {
        model.addFlow(flows.pop());
        flowContexts.pop();
    }

    @Override
    public void enterExpression(VirtDataParser.ExpressionContext ctx) {
        expressionContexts.push(ctx);
        flows.peek().addExpression(new Expression());
        //logger.debug("parsing virtdata expression.");
    }

    @Override
    public void exitLvalue(VirtDataParser.LvalueContext ctx) {
        flows.peek().getLastExpression().setAssignment(new Assignment(ctx.ID().getSymbol().getText()));
    }

    @Override
    public void exitExpression(VirtDataParser.ExpressionContext ctx) {
        expressionContexts.pop();
    }

    @Override
    public void enterVirtdataCall(VirtDataParser.VirtdataCallContext ctx) {
        callContexts.push(ctx);
        calls.push(new FunctionCall());
    }

    @Override
    public void exitVirtdataCall(VirtDataParser.VirtdataCallContext ctx) {

        FunctionCall topFunctionCall = calls.pop();
        if (calls.empty()) {
            flows.peek().getLastExpression().setCall(topFunctionCall);
        } else {
            calls.peek().addFunctionArg(topFunctionCall);
        }

        callContexts.pop();
    }

    @Override
    public void exitInputType(VirtDataParser.InputTypeContext ctx) {
        calls.peek().setInputType(ctx.getText());
    }

    @Override
    public void exitFuncName(VirtDataParser.FuncNameContext ctx) {
        calls.peek().setFuncName(ctx.getText());
    }

    @Override
    public void exitOutputType(VirtDataParser.OutputTypeContext ctx) {
        calls.peek().setOutputType(ctx.getText());
    }

    @Override
    public void exitRef(VirtDataParser.RefContext ctx) {
        calls.peek().addFunctionArg(new RefArg(ctx.ID().getText()));
    }

    @Override
    public void exitIntegerValue(VirtDataParser.IntegerValueContext ctx) {
        calls.peek().addFunctionArg(new IntegerArg(Integer.valueOf(ctx.getText())));
    }

    @Override
    public void exitFloatValue(VirtDataParser.FloatValueContext ctx) {
        calls.peek().addFunctionArg(new FloatArg(Float.valueOf(ctx.getText())));
    }

    @Override
    public void exitStringValue(VirtDataParser.StringValueContext ctx) {
        calls.peek().addFunctionArg(new StringArg(ctx.getText().substring(1, ctx.getText().length() - 1)));
    }

    @Override
    public void exitDoubleValue(VirtDataParser.DoubleValueContext ctx) {
        calls.peek().addFunctionArg(new DoubleArg(Double.valueOf(ctx.getText())));
    }

    @Override
    public void exitLongValue(VirtDataParser.LongValueContext ctx) {
        calls.peek().addFunctionArg(new LongArg(Long.valueOf(ctx.getText())));
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
