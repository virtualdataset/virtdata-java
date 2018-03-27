package io.virtdata.api;

import io.virtdata.core.ResolvedFunction;
import org.apache.commons.lang3.ClassUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BasicFunctionalLibrary implements VirtDataFunctionLibrary {
    private final static Logger logger = LoggerFactory.getLogger(BasicFunctionalLibrary.class);

    @Override
    public abstract String getName();

    public abstract List<Package> getSearchPackages();

    @Override
    public List<ResolvedFunction> resolveFunctions(Class<?> returnType, Class<?> inputType, String functionName, Object... parameters) {

        // TODO: Make this look for both assignment compatible matches as well as exact assignment matches, and only
        // TODO: return assignment compatible matches when there are none exact matching.
        // TODO: Further, make lambda construction honor exact matches first as well.

        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();
        List<Class<?>> matchingClasses = getSearchPackages().stream()
                .map(p -> p.getName() + "." + functionName)
                .map(this::maybeClassForName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Constructor<?>> matchingConstructors = matchingClasses.stream()
                .filter(c -> {
                    // This form for debugging
                    boolean isFunctional = isFunctionalInterface(c);
                    boolean canAssignInput = inputType==null || canAssignInputType(c, inputType);
                    boolean canAssignReturn = returnType==null ||canAssignReturnType(c, returnType);
                    boolean matchesSignature = isFunctional && canAssignInput && canAssignReturn;
                    return matchesSignature;
                })
                .flatMap(c -> Arrays.stream(c.getDeclaredConstructors()))
                .filter(c -> {
                    boolean canAssignArgv = canAssignArguments(c, parameters);
                    return canAssignArgv;
                })
                .collect(Collectors.toList());

        if (returnType != null && inputType != null && matchingConstructors.size() > 1) {
            throw new RuntimeException(
                    "found more than one (" + matchingConstructors.size() + ") matching constructor for " +
                            "return type '" + returnType + "', " +
                            "inputType '" + inputType + "', " +
                            "function name '" + functionName + ", " +
                            "and parameter types '" + Arrays.toString(parameters) + "', " +
                            "ctors: " + matchingConstructors);

        }

        for (Constructor<?> ctor : matchingConstructors) {
            try {
                Object func = ctor.newInstance(parameters);
                boolean threadSafe = ctor.getClass().getAnnotation(ThreadSafeMapper.class) != null;
                resolvedFunctions.add(
                        new ResolvedFunction(
                                func, threadSafe, ctor.getParameterTypes(), parameters,
                                getInputClass(ctor.getDeclaringClass()),
                                getOutputClas(ctor.getDeclaringClass()),
                                this.getName())
                );
            } catch (Exception e) {
                throw new RuntimeException("Error while calling constructor '" + ctor.toString() + "': " + e, e);
            }
        }

        return resolvedFunctions;
    }

    private boolean isFunctionalInterface(Class<?> c) {
        Optional<Method> applyMethods = Arrays.stream(c.getMethods())
                .filter(m -> {
                    boolean isNotDefault = !m.isDefault();
                    boolean isNotBridge = !m.isBridge();
                    boolean isNotSynthetic = !m.isSynthetic();
                    boolean isPublic = (m.getModifiers()&Modifier.PUBLIC)>0;
                    boolean isNotString = !m.getName().equals("toString");
                    boolean isApplyMethod = m.getName().startsWith("apply");
                    return isNotDefault && isNotBridge && isNotSynthetic && isPublic && isNotString && isApplyMethod;
                })
                .findFirst();
        return applyMethods.isPresent();
    }

    // TODO: Make this work with varargs constructors
    private boolean canAssignArguments(Constructor<?> targetConstructor, Object[] sourceParameters) {
        Class<?>[] targetTypes = targetConstructor.getParameterTypes();
        if (sourceParameters.length != targetTypes.length) {
            logger.trace(targetConstructor.toString() + " does not match source parameters (size): " + Arrays.toString(sourceParameters));
            return false;
        }
        Class<?>[] sourceTypes = new Class<?>[sourceParameters.length];
        for (int i = 0; i < sourceTypes.length; i++) {
            sourceTypes[i]=sourceParameters[i].getClass();
        }
//        targetConstructor.get

        boolean isAssignable = ClassUtils.isAssignable(sourceTypes, targetTypes, true);
        return isAssignable;
//        for (int i = 0; i < targetTypes.length; i++) {
//            Class<?> targetType = targetTypes[i];
//            Class<?> sourceType = sourceParameters[i].getClass();
//            if (!ClassUtils.isAssignable(sourceType, targetType,true)
//                    && !targetType.isAssignableFrom(sourceType)) {
////            if (!targetType.isAssignableFrom(sourceType)) {
//                logger.trace(targetConstructor.toString() + " is not assignable from input types (pair-wise): " + Arrays.toString(sourceParameters));
//                return false;
//            }
//        }
//        return true;
    }

    private boolean canAssignReturnType(Class<?> functionalClass, Class<?> returnType) {
        Class<?> sourceType = toFunctionalMethod(functionalClass).getReturnType();
        boolean isAssignable = returnType.isAssignableFrom(sourceType);
        return isAssignable;
    }

    private Class<?> getInputClass(Class<?> functionalClass) {
        return toFunctionalMethod(functionalClass).getParameterTypes()[0];
    }
    private Class<?> getOutputClas(Class<?> functionClass) {
        return toFunctionalMethod(functionClass).getReturnType();
    }

    private boolean canAssignInputType(Class<?> functionalClass, Class<?> inputType) {
        boolean isAssignable = toFunctionalMethod(functionalClass).getParameterTypes()[0].isAssignableFrom(inputType);
        return isAssignable;
    }

    private Class<?> maybeClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private Method toFunctionalMethod(Class<?> clazz) {

        Optional<Method> foundMethod = Arrays.stream(clazz.getMethods())
                .filter(m -> !m.isDefault() && !m.isBridge() && !m.isSynthetic())
                .filter(m -> m.getName().startsWith("apply"))
                .findFirst();

        return foundMethod.orElseThrow(
                () -> new RuntimeException(
                        "Unable to find the function method on " + clazz.getCanonicalName()
                )
        );
    }

    @Override
    public List<String> getDataMapperNames() {

        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner());
        cb.setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])));

        FilterBuilder fb = new FilterBuilder();

        for (Package aPackage : getSearchPackages()) {
            fb.include(FilterBuilder.prefix(aPackage.getName()));
        }
        cb.filterInputsBy(fb);
        Reflections reflections = new Reflections(cb);

        Set<Class<?>> subTypesOf =
                reflections.getSubTypesOf(Object.class);

        ArrayList<String> collected = subTypesOf.stream()
                .map(Class::getSimpleName)
                .collect(Collectors.toCollection(ArrayList::new));

        return collected;

    }

    public String toString() {
        return getName();
    }

}
