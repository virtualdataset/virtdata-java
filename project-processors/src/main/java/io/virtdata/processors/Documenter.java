package io.virtdata.processors;

import io.virtdata.annotations.*;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;

public class Documenter {
    private final Element element;
    private Filer filer;

    public Documenter(Element element, Filer filer) {
        this.element = element;
        this.filer = filer;
    }

    public void write() {
        StdoutElementVisitor ev = new StdoutElementVisitor();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = element.accept(ev,sb);
        System.out.println(sb2.toString());
//        StdoutTypeVisitor v = new StdoutTypeVisitor();
    }

    public void write2() {
//        System.out.println("writing for " + element);
        StringBuilder sb = new StringBuilder();

        Class<ElementKind> declaringClass = element.getKind().getDeclaringClass();

        get(Description.class).ifPresent(d -> sb.append("### ").append(d.value()).append("\n"));

        //        element.getKind().isClass()
        // ((TypeElement) element).getInterfaces()
                // element.getSimpleName()
        // ((TypeElement) element).getInterfaces()
        // element.asType()
        sb.append("input type: ").append(getInputType()).append("\n");
        sb.append("output type: ").append(getOutputType()).append("\n");


        get(DeprecatedFunction.class).ifPresent(d -> sb.append("*_deprecated_*\n"));
        get(Examples.class).ifPresent(e -> {
            sb.append("### Examples:\n");
            for (Example example : e.value()) {
                String[] value = example.value();
                String code = value[0];
                String desc = value.length == 2 ? "// " + value[1] : "";
                sb.append("`").append(code);
                sb.append(" ").append(desc).append("`\n");
            }
        });
//        Example example = element.getAnnotation(Example.class)
        sb.append(get(ThreadSafeMapper.class).map(t -> "✓threadsafe\n").orElse("⚠per-thread\n"));
        get(SeeList.class).ifPresent(s -> {
            sb.append("*See Also:*\n");
            for (See see : s.value()) {
                sb.append("- [").append(see.name()).append("](").append(see.url()).append("]\n");
            }
        });
        See see = element.getAnnotation(See.class);
        System.out.println(sb.toString());
    }

    private TypeMirror getInterface() {
        List<? extends TypeMirror> interfaces = ((TypeElement) element).getInterfaces();
        if (interfaces.size()>1) {
            throw new RuntimeException("Unable to find input type on functional type with multiple interfaces '" + element + ": " + interfaces.toString());
        }
        return interfaces.get(0);
    }

    private String getInputType() {
        return TypeMap.valueOfName(getInterface().toString()).getInputClass().getSimpleName();
    }
    private String getOutputType() {
        return TypeMap.valueOfName(getInterface().toString()).getOutputClass().getSimpleName();
    }

    <A extends Annotation> Optional<A> get(Class<A> annotationType) {
        A annotation = element.getAnnotation(annotationType);
        return Optional.ofNullable(annotation);
    }

    private enum TypeMap {
        LongUnaryOperator(java.util.function.LongUnaryOperator.class.getCanonicalName(),long.class,long.class),
        IntUnaryOperator(java.util.function.IntUnaryOperator.class.getCanonicalName(),int.class, int.class),
        IntFunction(java.util.function.IntFunction.class.getCanonicalName(),int.class,Object.class),
        LongFunction(java.util.function.LongFunction.class.getCanonicalName(),long.class,Object.class),
        LongToIntFunction(java.util.function.LongToIntFunction.class.getCanonicalName(),long.class,int.class),
        DoubleToIntFunction(java.util.function.DoubleToIntFunction.class.getCanonicalName(),double.class,int.class),
        Function(java.util.function.Function.class.getCanonicalName(),Object.class,Object.class);

        private final String typeName;
        private final Class<?> inputClass;
        private final Class<?> outputClass;

        TypeMap(String typeName, Class<?> inputClass, Class<?> outputClass) {
            this.typeName = typeName;
            this.inputClass = inputClass;
            this.outputClass = outputClass;
        }

        public static TypeMap valueOfName(String typeName) {
            for (TypeMap typeMap : TypeMap.values()) {
                if (typeMap.getTypeName().equals(typeName)) {
                    return typeMap;
                }
            }
            throw new RuntimeException("Unable to map function type to TypeMap:" + typeName);
        }

        public String getTypeName() {
            return typeName;
        }

        public Class<?> getInputClass() {
            return inputClass;
        }
        public Class<?> getOutputClass() {
            return outputClass;
        }

    }

}
