package io.virtdata.core;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.services.FunctionFinderService;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class VirtDataFunctionResolver {
    private final static Logger logger = LoggerFactory.getLogger(VirtDataFunctionResolver.class);
    private final static MethodHandles.Lookup lookup = MethodHandles.publicLookup();
    private final FunctionFinder functionFinder = new FunctionFinder();

    public List<ResolvedFunction> resolveFunctions(
            Class<?> returnType,
            Class<?> inputType,
            String functionName,
            Object... parameters) {

        StringBuilder rs = new StringBuilder();
        rs.append((inputType == null ? "ANY" : inputType.getSimpleName()));
        rs.append("->").append(functionName).append("(");
        rs.append(Arrays.stream(parameters).map(Object::getClass).map(Class::getSimpleName).collect(Collectors.joining(",")));
        rs.append(")->");
        rs.append((returnType == null ? "ANY" : returnType.getSimpleName()));
        String requestedSignature = rs.toString();

        // TODO: Make this look for both assignment compatible matches as well as exact assignment matches, and only
        // TODO: return assignment compatible matches when there are none exact matching.
        // TODO: Further, make lambda construction honor exact matches first as well.

        Class<?>[] parameterTypes = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }

        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();

        List<FunctionFinderService.Path> functionNames = functionFinder.getFunctionNames();

        logger.trace("considering " + functionNames.size() + " different functions for '" + requestedSignature);
        List<Class<?>> matchingClasses = functionNames
                .stream()
//                .filter(pat -> {
//                    logger.debug("p.className=" + pat.className + " funcName=" + functionName);
//                    return true;
//                })
                .filter(p -> p.className.endsWith("." + functionName))
                .map(this::maybeClassForName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        logger.trace("found " + matchingClasses.size() + " matching classes with the same simple name '" + functionName + "'");

        List<Constructor<?>> matchingConstructors = null;
        matchingConstructors = matchingClasses.stream()
                .filter(c -> {
                    // This form for debugging
                    boolean isFunctional = isFunctionalInterface(c);
                    boolean canAssignInput = inputType == null || canAssignInputType(c, inputType);
                    boolean canAssignReturn = returnType == null || canAssignReturnType(c, returnType);
                    boolean matchesSignature = isFunctional && canAssignInput && canAssignReturn;
                    if (!matchesSignature) {
                        logger.trace("rejected: functional?=" + isFunctional + ", input type ok?=" + canAssignInput +
                                ", output type ok?=" + canAssignReturn);
                    }
                    return matchesSignature;
                })
                .flatMap(c -> Arrays.stream(c.getDeclaredConstructors()))
                .filter(c -> {
                    logger.trace("considering " + getEffectiveSignature(c));

                    Class<?>[] ctypes = c.getParameterTypes();

                    if (c.isVarArgs()) {
                        if (!ClassUtils.isAssignable(
                                Arrays.copyOfRange(parameterTypes, 0, ctypes.length - 1),
                                Arrays.copyOfRange(ctypes, 0, ctypes.length - 1),
                                true)) {
                            logger.trace(" rejected: varargs types are not assignable from the input values");
                            return false;
                        }

                        Class<?> componentType = ctypes[ctypes.length - 1].getComponentType();
                        if (parameterTypes.length >= ctypes.length && !ClassUtils.isAssignable(parameterTypes[ctypes.length - 1], componentType, true)) {
                            logger.trace(" rejected: varargs type is not assignable from the input value type");
                            return false;
                        }
                        return true;
                    } else {
                        if (parameterTypes.length != ctypes.length) {
                            logger.trace(" rejected: the ctor has " + ctypes.length + " parameters, but " + parameterTypes.length + " are provided.");
                            return false;
                        }
                        boolean assignable = ClassUtils.isAssignable(parameterTypes, ctypes, true);
                        if (!assignable) {
                            logger.trace(" rejected: parameter types are not assignable to signature");
                        }
                        return assignable;
                    }

                })
                .collect(Collectors.toList());

        logger.debug("found " + matchingConstructors.size() + " matching constructors which can fulfill " +
                requestedSignature);
        if (!logger.isTraceEnabled()) {
            logger.debug("see TRACE level logs for details.");
        }

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
                Class<?> ctorDClass = ctor.getDeclaringClass();
                MethodType ctorMethodType = MethodType.methodType(void.class, ctor.getParameterTypes());
                MethodHandle constructor = lookup.findConstructor(ctorDClass, ctorMethodType);
                Object functionalInstance = constructor.invokeWithArguments(parameters);
                boolean threadSafe = functionalInstance.getClass().getAnnotation(ThreadSafeMapper.class) != null;
                resolvedFunctions.add(
                        new ResolvedFunction(
                                functionalInstance,
                                threadSafe,
                                parameterTypes,
                                parameters,
                                getInputClass(functionalInstance.getClass()),
                                getOutputClas(functionalInstance.getClass())
                        )
                );
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
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
                    boolean isPublic = (m.getModifiers() & Modifier.PUBLIC) > 0;
                    boolean isNotString = !m.getName().equals("toString");
                    boolean isApplyMethod = m.getName().startsWith("apply");
                    boolean isFunctional = isNotDefault && isNotBridge && isNotSynthetic && isPublic && isNotString && isApplyMethod;
                    return isFunctional;
                })
                .findFirst();
        return applyMethods.isPresent();
    }

    private boolean canAssignArguments(Constructor<?> targetCtor, Object[] sourceParameters) {
        boolean isAssignable = true;
        Class<?>[] targetTypes = targetCtor.getParameterTypes();

        if (targetCtor.isVarArgs()) {
            if (sourceParameters.length < (targetTypes.length - 1)) {
                logger.trace(targetCtor.toString() + " (varargs) does not match, not enough source parameters: " + Arrays.toString(sourceParameters));
                return false;
            }
        } else if (sourceParameters.length != targetTypes.length) {
            logger.trace(targetCtor.toString() + " (varargs) does not match source parameters (size): " + Arrays.toString(sourceParameters));
            return false;
        }

        Class<?>[] sourceTypes = new Class<?>[sourceParameters.length];
        for (int i = 0; i < sourceTypes.length; i++) {
            sourceTypes[i] = sourceParameters[i].getClass();
        }

        if (targetCtor.isVarArgs()) {
            for (int i = 0; i < targetTypes.length - 1; i++) {
                if (!ClassUtils.isAssignable(sourceTypes[i], targetTypes[i])) {
                    isAssignable = false;
                    break;
                }
            }
            Class<?> componentType = targetTypes[targetTypes.length - 1].getComponentType();
            for (int i = targetTypes.length - 1; i < sourceTypes.length; i++) {
                if (!ClassUtils.isAssignable(sourceTypes[i], componentType, true)) {

                    isAssignable = false;
                    break;
                }
            }
        } else {
            for (int i = 0; i < targetTypes.length; i++) {
                if (!ClassUtils.isAssignable(sourceTypes[i], targetTypes[i])) {
                    isAssignable = false;
                    break;
                }
            }
//
//            isAssignable = ClassUtils.isAssignable(sourceTypes, targetTypes, true);
        }
        return isAssignable;
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

    private Class<?> maybeClassForName(FunctionFinderService.Path path) {
        try {
            return Class.forName(path.finder.getClass().getModule(), path.className);
        } catch (Exception e) {
            return null;
        }
    }

    private String getEffectiveSignature(Constructor ctor) {
        StringBuilder sb = new StringBuilder();
        Method fmethod = toFunctionalMethod(ctor.getDeclaringClass());
        sb.append(fmethod.getParameterTypes()[0].getTypeName()).append("->");
        sb.append(ctor.getDeclaringClass().getCanonicalName()).append("(");
        for (Class<?> parameterType : ctor.getParameterTypes()) {
            sb.append(parameterType.getTypeName()).append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append(")->");
        sb.append(fmethod.getReturnType().getTypeName());
        return sb.toString();
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

    public List<FunctionFinderService.Path> getFunctionNames() {
        return functionFinder.getFunctionNames();
    }


}
