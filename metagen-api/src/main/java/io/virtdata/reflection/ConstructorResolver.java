package io.virtdata.reflection;

import io.virtdata.util.StringObjectPromoter;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Match (class,string,...) constructor signatures to a matching converted signature,
 * in Object[] form.
 */
public class ConstructorResolver {
    private final static Logger logger = LoggerFactory.getLogger(ConstructorResolver.class);

    private Exception lastException;

    public static <T> T resolveAndConstruct(String[] classAndArgs) {
        DeferredConstructor<T> deferredConstructor = createDeferredConstructor(classAndArgs);
        T constructed = deferredConstructor.construct();
        return constructed;
    }

    public static <T> DeferredConstructor<T> resolve(String[] classAndArgs) {
        DeferredConstructor<T> deferredConstructor = createDeferredConstructor(classAndArgs);
        return deferredConstructor;
    }

    public static <T> DeferredConstructor<T> resolve(Class<T> clazz, String... args) {
        DeferredConstructor<T> deferredConstructor = createDeferredConstructor(clazz, args);
        return deferredConstructor;
    }

    @SuppressWarnings("unchecked")
    public static <T> DeferredConstructor<T> resolve(String className, String[] args) {
        Class<T> clazz = null;
        try {
            clazz = (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        DeferredConstructor<T> deferredConstructor = createDeferredConstructor(clazz, args);
        return deferredConstructor;
    }

    @SuppressWarnings("unchecked")
    private static <T> DeferredConstructor<T> createDeferredConstructor(String[] signature) {
        String className = signature[0];

        if (!className.contains(".")) {
            throw new RuntimeException(ConstructorResolver.class.getSimpleName() + " needs a fully qualified package name.");
        }
        Class<T> clazz;
        try {
            clazz = (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return createDeferredConstructor(clazz, Arrays.copyOfRange(signature, 1, signature.length));
    }

    private static Predicate<Constructor> canAssignToConstructor(String[] args) {
        return new Predicate<Constructor>() {
            @Override
            public boolean test(Constructor ctor) {
                Class<?>[] ptypes = ctor.getParameterTypes();
                for (int paramidx = 0; paramidx < ptypes.length; paramidx++) {
                    Class<?> ptype = ptypes[paramidx];
                    Object promotedArg = StringObjectPromoter.promote(args[paramidx],ptype);
                    if (!StringObjectPromoter.isAssignableForConstructor(promotedArg.getClass(),ptype)) {
                        logger.trace("removing " + ctor + " from candidate list, because arg " + paramidx
                                + " is not assignable from promoted type");
                        return false;
                    }
                }
                return true;
            }
        };
    }

    private static <T> DeferredConstructor<T> createDeferredConstructor(Class<T> clazz, String... args) {

        List<Constructor> matchingConstructors = new ArrayList<>();

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == args.length) {
                matchingConstructors.add(constructor);
            }
        }

        matchingConstructors = matchingConstructors.stream()
                .filter(canAssignToConstructor(args)).collect(Collectors.toList());

        if (matchingConstructors.size() == 0) {
            throw new RuntimeException("no constructor found for " + clazz.getSimpleName() + " with " +
                    (args.length) + " parameters");
        }

        if (matchingConstructors.size() > 1) {
            List<String> signatures = new ArrayList<String>();
            for (Constructor matchingConstructor : matchingConstructors) {
                Class[] pt = matchingConstructor.getParameterTypes();
                signatures.add(
                        Arrays.stream(pt)
                                .map(Class::getSimpleName)
                                .collect(Collectors.joining(",", "(", ")"))
                );
            }
            String diagnosticList = signatures.stream().collect(Collectors.joining(",", "[", "]"));

            throw new RuntimeException("Multiple constructors found for " + clazz.getSimpleName() + " with " +
                    (args.length) + " parameters:" + diagnosticList
            );
        }

        Constructor matchingConstructor = matchingConstructors.get(0);
        Object[] argsArray = new Object[matchingConstructor.getParameterCount()];
        for (int i = 0; i < argsArray.length; i++) {
            String argString = args[i];
            Object mapped = StringMapper.mapValue(argString, matchingConstructor.getParameterTypes()[i]);
            argsArray[i] = mapped;
        }

        // sanity check
        try {
            ConstructorUtils.invokeConstructor(clazz, argsArray);
        } catch (Exception e) {
            throw new RuntimeException("Unable to invoke constructor as sanity check for args:" +
                    Arrays.toString(argsArray), e);
        }

        DeferredConstructor<T> dc = new DeferredConstructor<>(clazz, argsArray);
        return dc;
    }

    private static enum StringMapper {

        // HINT: Do NOT put more than primitives or very common types here

        STRING(String.class, null, (String i) -> i),
        INTEGER(Integer.class, int.class, Integer::valueOf),
        BIGDECIMAL(BigDecimal.class, null, (String i) -> BigDecimal.valueOf(Long.valueOf(i))),
        BOOLEAN(Boolean.class, boolean.class, Boolean::valueOf),
        SHORT(Short.class, short.class, Short::valueOf),
        BYTE(Byte.class, byte.class, Byte::valueOf),
        DOUBLE(Double.class, double.class, Double::valueOf),
        CHAR(Character.class, char.class, (String c) -> c.charAt(0)),
        FLOAT(Float.class, float.class, Float::valueOf),
        LONG(Long.class, long.class, Long::valueOf);

        private final Class<?> targetClass;
        private final Class<?> primitiveClass;
        private final Function<String, ?> mapperFunction;

        <T> StringMapper(Class<T> targetClass, Class<?> primitiveName, Function<String, T> mapperFunction) {
            this.targetClass = targetClass;
            this.mapperFunction = mapperFunction;
            this.primitiveClass = primitiveName;
        }

        public static Optional<StringMapper> valueOf(Class<?> targetClass) {
            for (StringMapper stringMapper : StringMapper.values()) {
                if (stringMapper.getTargetClass().equals(targetClass)) {
                    return Optional.of(stringMapper);
                }
                if (stringMapper.primitiveClass != null && stringMapper.primitiveClass.equals(targetClass)) {
                    return Optional.of(stringMapper);
                }
            }
            throw new RuntimeException("StringMapper could not match " + targetClass);
        }

        public static Object mapValue(String value, Class<?> targetClass) {
            Optional<StringMapper> mapper = StringMapper.valueOf(targetClass);
            if (mapper.isPresent()) {
                return mapper.get().getMapperFunction().apply(value);
            } else {
                throw new RuntimeException(
                        "Unable to find type mapper for String and class " + targetClass.getCanonicalName()
                );
            }

        }

        public Class<?> getTargetClass() {
            return targetClass;
        }

        public Function<String, ?> getMapperFunction() {
            return mapperFunction;
        }


    }



}
