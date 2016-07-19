package io.virtdata.gen.internal;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DeferredConstructor<T> {
    private Class<T> classToConstruct;
    private Object[] args;

    public DeferredConstructor(Class<T> classToConstruct, Object... args) {
        this.classToConstruct = classToConstruct;
        this.args = args;
    }

    public T construct() {
        T constructed = null;
        try {
            constructed = ConstructorUtils.invokeConstructor(classToConstruct, args);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking constructor for:" + this.toString());
        }
        return constructed;
    }

    public String toString() {
        return "class:" + classToConstruct.getName() + ", args:" +
                Arrays.stream(args)
                        .map(String::valueOf)
                        .collect(Collectors.joining("[", ",", "]"));
    }

    public void validate() {
        try {
            construct();
        } catch (Exception e) {
            throw new RuntimeException("Error while validating DeferredConstructor for " + this.toString(), e);
        }

    }
}
