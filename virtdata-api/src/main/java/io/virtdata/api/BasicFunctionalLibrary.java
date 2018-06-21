package io.virtdata.api;

import io.virtdata.annotations.ThreadSafeMapper;
import io.virtdata.core.ResolvedFunction;
import io.virtdata.processors.DocFuncData;
import io.virtdata.processors.VirtDataLibraryInfo;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BasicFunctionalLibrary implements VirtDataFunctionLibrary, EnhancedDocs {
    private final static Logger logger = LoggerFactory.getLogger(BasicFunctionalLibrary.class);

    private List<String> searchPackages;

    @Override
    public abstract String getName();

    public abstract List<Package> getSearchPackages();

    private synchronized List<String> getAllPackageNames() {
        if (searchPackages==null) {
            Set<String> packageNames = new HashSet<>();
            getSearchPackages().stream().map(Package::getName).forEachOrdered(packageNames::add);
            packageNames.addAll(this.getDocPackages());
            searchPackages= new ArrayList<>(packageNames);
        }
        return searchPackages;
    }

    @Override
    public List<ResolvedFunction> resolveFunctions(Class<?> returnType, Class<?> inputType, String functionName, Object... parameters) {

        // TODO: Make this look for both assignment compatible matches as well as exact assignment matches, and only
        // TODO: return assignment compatible matches when there are none exact matching.
        // TODO: Further, make lambda construction honor exact matches first as well.

        List<ResolvedFunction> resolvedFunctions = new ArrayList<>();
        List<Class<?>> matchingClasses = getAllPackageNames().stream()
                .map(p -> p + "." + functionName)
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
            Object[] args=parameters;
            if (ctor.isVarArgs()) {
                args = new Object[ctor.getParameterCount()];
                int varArgsCount = (parameters.length - ctor.getParameterCount())+1;
                Class<?> varargArrayType = ctor.getParameterTypes()[ctor.getParameterTypes().length - 1];
                Class<?> varArgType = varargArrayType.getComponentType();
                Object varArgs = Array.newInstance(varArgType, varArgsCount);
                for (int i = 0; i < parameters.length; i++) {
                    if (i<args.length-1) {
                        args[i] = parameters[i];
                    } else {
                        ((Object[])varArgs)[(i-args.length)+1]=varArgType.cast(parameters[i]);
                    }
                }
                args[args.length-1]=varArgs;
            }

            try {
                Object func = ctor.newInstance((Object[])args);
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
                    boolean isFunctional = isNotDefault && isNotBridge && isNotSynthetic && isPublic && isNotString && isApplyMethod;
                    return isFunctional;
                })
                .findFirst();
        return applyMethods.isPresent();
    }

    private boolean canAssignArguments(Constructor<?> targetConstructor, Object[] sourceParameters) {
        boolean isAssignable=true;
        Class<?>[] targetTypes = targetConstructor.getParameterTypes();

        if (targetConstructor.isVarArgs()) {
            if (sourceParameters.length< (targetTypes.length-1)) {
                logger.trace(targetConstructor.toString() + " (varargs) does not match, not enough source parameters: " + Arrays.toString(sourceParameters));
                return false;
            }
        } else if (sourceParameters.length != targetTypes.length) {
            logger.trace(targetConstructor.toString() + " (varargs) does not match source parameters (size): " + Arrays.toString(sourceParameters));
            return false;
        }

        Class<?>[] sourceTypes = new Class<?>[sourceParameters.length];
        for (int i = 0; i < sourceTypes.length; i++) {
            sourceTypes[i]=sourceParameters[i].getClass();
        }

        if (targetConstructor.isVarArgs()) {
            for (int i = 0; i < targetTypes.length-1; i++) {
                if (!ClassUtils.isAssignable(sourceTypes[i],targetTypes[i])) {
                    isAssignable=false;
                    break;
                }
            }
            Class<?> componentType = targetTypes[targetTypes.length-1].getComponentType();
            for (int i = targetTypes.length-1; i < sourceTypes.length; i++) {
                if (!ClassUtils.isAssignable(sourceTypes[i],componentType,true)) {

                    isAssignable=false;
                    break;
                }
            }
        } else {
            for (int i = 0; i < targetTypes.length; i++) {
                if (!ClassUtils.isAssignable(sourceTypes[i],targetTypes[i])) {
                    isAssignable=false;
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
    public List<DocFuncData> getDocModels() {
        List<DocFuncData> docs = new ArrayList<>();

        String docsClassName = this.getClass().getCanonicalName()+"AutoDocsInfo";
        Class<? extends VirtDataLibraryInfo> docsClass =null;
        try {
            docsClass = Class.forName(docsClassName).asSubclass(VirtDataLibraryInfo.class);
        } catch (ClassNotFoundException e) {
            logger.warn("Unable to find documentation class " + docsClassName);
            return docs;
        }

        try {
            VirtDataLibraryInfo enhancedDocs = docsClass.newInstance();
            return enhancedDocs.getDocsInfo();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<String> getDataMapperNames() {
        List<DocFuncData> docModels = getDocModels();
        return docModels.stream().map(DocFuncData::getClassName).collect(Collectors.toList());

    }

    public String toString() {
        return getName();
    }

}
