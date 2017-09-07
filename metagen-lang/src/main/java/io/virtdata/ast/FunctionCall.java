package io.virtdata.ast;

import java.util.ArrayList;
import java.util.List;

public class FunctionCall implements ArgType {
    private String funcName;
    private List<ArgType> args = new ArrayList<>();
    private String inputType;
    private String outputType;

    public FunctionCall() {}

    public FunctionCall(String funcName) {
        this.funcName = funcName;
    }

    public FunctionCall(String inputType, String funcName) {
        this.inputType = inputType;
        this.funcName = funcName;
    }

    public void addFunctionArg(ArgType argType) {
        this.args.add(argType);
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(inputType==null ? "" : inputType + ">-");
        sb.append(funcName);
        sb.append("(");
        String sep = "";
        for (ArgType arg : args) {
            sb.append(sep);
            sb.append(arg);
            sep=",";
        }
        sb.append(")");
        sb.append(outputType==null ? "" : "->"+ outputType);
        return sb.toString();
    }

    public FunctionCall getMetagenCall(int i) {
        return (FunctionCall) args.get(i);
    }

    public String getFunctionName() {
        return funcName;
    }

    public String getInputType() {
        return inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public List<ArgType> getArgs() {
        return this.args;
    }
}
