package io.virtdata.core;

import io.virtdata.api.FunctionType;
import io.virtdata.api.GeneratorLibrary;

/**
 * A function that has been resolved by a library for use in data mapping.
 * Some API calls require this type, as it can only be constructed successfully
 * if the object type is valid for mapping to a generator.
 */
public class ResolvedFunction {

    private FunctionType functionType;
    private Object functionObject;
    private GeneratorLibrary library;

    public ResolvedFunction(Object g, GeneratorLibrary library) {
        this.library = library;
        functionObject = g;
        functionType = FunctionType.valueOf(g); // sanity check the type of g
    }
    public ResolvedFunction(Object g) {
        this.functionObject = g;
        functionType = FunctionType.valueOf(g);  // sanity check the type of g
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

    public String toString() {
        return "FunctionType:" + functionType
                + ", lib:" + library.getLibraryName()
                + ", fn:" + functionObject;
    }
}
