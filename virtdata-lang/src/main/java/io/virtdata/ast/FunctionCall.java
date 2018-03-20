package io.virtdata.ast;

import java.util.ArrayList;
import java.util.List;

public class FunctionCall implements ArgType {
    private String funcName;
    private List<ArgType> args = new ArrayList<>();
    private String inputType;
    private String outputType;
    private String inputClass;

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
        sb.append(inputType==null ? "" : inputType + "->");
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

    public FunctionCall getVirtdataCall(int i) {
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

    public Object[] getArguments() {
        List<Object> args = new ArrayList<>();
        for (ArgType argType : getArgs()) {
            ArgType.TypeName typeName = ArgType.TypeName.valueOf(argType);
            switch (typeName) {
                case RefArg:
                    args.add(new VariableRef(((RefArg) argType).getRefName()));
                    break;
                case FunctionCall:
                    args.add(argType); // TODO: revisit this
                    break;
                case StringArg:
                    args.add(((StringArg) argType).getStringValue());
                    break;
                case FloatArg:
                    args.add(((FloatArg) argType).getFloatValue());
                    break;
                case IntegerArg:
                    args.add(((IntegerArg) argType).getIntValue());
                    break;
                case LongArg:
                    args.add(((LongArg) argType).getLongValue());
                    break;
                case DoubleArg:
                    args.add(((DoubleArg) argType).getDoubleValue());
                    break;
                default:
                    throw new RuntimeException("Could not map type into argument object: " + typeName);
            }
        }
        return args.toArray();
    }

    public String getInputClass() {
        return inputClass;
    }

//    public Optional<Class<?>> getInputClass() {
//        return classForFreeformTypeName(getInputType());
//    }
//    public Optional<Class<?>> getReturnClass() {
//        return classForFreeformTypeName(getOutputType());
//    }
//
//    private static Optional<Class<?>> classForFreeformTypeName(String basicTypeName) {
//        ValueType.
////        ValueType.valueOf
////        if (basicTypeName==null) {
////            return Optional.empty();
////        }
////        String canonicalType = basicTypeName;
//////        String canonicalType = basicTypeName.contains(".") ? basicTypeName : "java.lang." + basicTypeName;
////        try {
////            return Optional.of(Class.forName(canonicalType));
////        } catch (ClassNotFoundException e) {
////            throw new RuntimeException("Unable to resolve class named '" + canonicalType +"', (originally just '" + basicTypeName +"'");
////        }
//    }

}
