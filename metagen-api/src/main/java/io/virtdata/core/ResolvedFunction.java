package io.virtdata.core;

import io.virtdata.api.types.FunctionType;
import io.virtdata.api.GeneratorLibrary;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

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

    public Class<?> getReturnType() {
        Method applyMethod = getMethod();
        return applyMethod.getReturnType();
    }

    public Class<?> getArgType() {
        Method applyMethod = getMethod();
        if (applyMethod.getParameterCount() != 1) {
            throw new RuntimeException(
                    "The parameter cound is supposed to be 1, but it was" + applyMethod.getParameterCount()
            );
        }
        return applyMethod.getParameterTypes()[0];
    }

    private Method getMethod() {
        Optional<Method> applyMethod = Arrays.stream(functionObject.getClass().getMethods())
                .filter(m -> m.getName().startsWith("apply"))
                .findFirst();

        return applyMethod.orElseThrow(
                () -> new RuntimeException(
                        "Unable to find the function method on " + functionObject.getClass().getCanonicalName()
                )
        );

    }

}
