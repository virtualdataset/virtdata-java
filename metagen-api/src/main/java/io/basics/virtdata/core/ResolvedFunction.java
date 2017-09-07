package io.basics.virtdata.core;

import com.strobel.reflection.Type;
import io.basics.virtdata.api.DataMapperLibrary;
import io.basics.virtdata.api.FunctionType;
import io.basics.virtdata.api.ValueType;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

/**
 * A function that has been resolved by a library for use in data mapping.
 * Some API calls require this type, as it can only be constructed successfully
 * if the object type is valid for mapping to a data mapper function.
 */
public class ResolvedFunction {

    private FunctionType functionType;
    private Object functionObject;
    private DataMapperLibrary library;
    private boolean isThreadSafe;
    // cache applyMethod, it's idempotent

    public ResolvedFunction(Object g, boolean isThreadSafe, DataMapperLibrary library) {
        this.library = library;
        functionObject = g;
        this.isThreadSafe = isThreadSafe;
        functionType = FunctionType.valueOf(g); // sanity check the type of g
    }

    public ResolvedFunction(Object g, boolean isThreadSafe) {
        this.functionObject = g;
        this.isThreadSafe = isThreadSafe;
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

    public DataMapperLibrary getLibrary() {
        return library;
    }

    public void setLibrary(DataMapperLibrary library) {
        this.library = library;
    }

    public String toString() {
        return "fn:" + functionObject.getClass().getCanonicalName() + ", type:" + functionType
                + ((library==null) ? "" : ", lib:" + library.getLibraryName());
    }

    public Class<?> getResultClass() {
        Method applyMethod = getMethod();
        Type<?> returnType = Type.of(functionObject.getClass()).getMethod(applyMethod.getName()).getReturnType();
        return returnType.getErasedClass();
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

        Optional<Method> foundMethod = Arrays.stream(functionObject.getClass().getMethods())
                .filter(m -> m.getName().startsWith("apply"))
                .findFirst();

        return foundMethod.orElseThrow(
                () -> new RuntimeException(
                        "Unable to find the function method on " + functionObject.getClass().getCanonicalName()
                )
        );
    }

    public static Comparator<ResolvedFunction> PREFERRED_TYPE_COMPARATOR = new PreferredTypeComparator();

    public boolean isThreadSafe() {
        return isThreadSafe;
    }

    /**
     * Compare two ResolvedFunctions by preferred input type and then by preferred output type.
     */
    private static class PreferredTypeComparator implements Comparator<ResolvedFunction> {

        @Override
        public int compare(ResolvedFunction o1, ResolvedFunction o2) {
            ValueType iv1 = ValueType.valueOfClass(o1.getArgType());
            ValueType iv2 = ValueType.valueOfClass(o2.getArgType());
            int inputComparison = iv1.compareTo(iv2);
            if (inputComparison != 0) {
                return inputComparison;
            }
            iv1 = ValueType.valueOfClass(o1.getResultClass());
            iv2 = ValueType.valueOfClass(o2.getResultClass());
            return iv1.compareTo(iv2);
        }
    }

}
