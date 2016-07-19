package io.virtdata.core;

import io.virtdata.api.FunctionType;
import io.virtdata.api.GeneratorLibrary;

public class ResolvedFunction {

    private FunctionType functionType;
    private Object functionObject;
    private GeneratorLibrary library;


    public ResolvedFunction(Object g, GeneratorLibrary library) {
        this.library = library;
        functionObject = g;
        functionType = FunctionType.valueOf(g); // sanity check the type of g
    }

    public FunctionType getFunctionType() {
        return functionType;
    }

    public void setFunctionType(FunctionType functionType) {
        this.functionType = functionType;
    }

    public Object getFunctionObject() {
        return functionObject;
    }

    public void setFunctionObject(Object functionObject) {
        this.functionObject = functionObject;
    }

    public GeneratorLibrary getLibrary() {
        return library;
    }

    public void setLibrary(GeneratorLibrary library) {
        this.library = library;
    }
}
