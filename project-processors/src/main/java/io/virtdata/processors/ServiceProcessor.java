package io.virtdata.processors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This annotation processor is responsible for adding services to the
 * <pre>classes/META-INF/services/servicename</pre> file for each
 * implemented and annotated service name.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"io.virtdata.annotations.Service"})
public class ServiceProcessor extends AbstractProcessor {

    private static Pattern packageNamePattern = Pattern.compile("(?<packageName>.+)?\\.(?<className>.+)");
    private Filer filer;
    private Map<String, String> options;
    private Elements elementUtils;
    private Messager messenger;
    private SourceVersion sourceVersion;
    private Types typeUtils;
    private Map<String, Writer> writers = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.options = processingEnv.getOptions();
        this.elementUtils = processingEnv.getElementUtils();
        this.messenger = processingEnv.getMessager();
        this.sourceVersion = processingEnv.getSourceVersion();
        this.typeUtils = processingEnv.getTypeUtils();
    }

    private Writer getWriterForClass(Class<?> clazz, Element... elements) {
        String serviceName = clazz.getCanonicalName();
        return writers.computeIfAbsent(serviceName, s -> {
            try {
                return filer.createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/services/" + s, elements)
                        .openWriter();
            } catch (IOException e) {
                messenger.printMessage(Diagnostic.Kind.ERROR, e.toString());
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        List<Element> ts = new ArrayList<>();

        try {
            for (String annotationType : this.getSupportedAnnotationTypes()) {
                Class<? extends Annotation> annotationClass =
                        (Class<? extends Annotation>) Class.forName(annotationType);
                Set<? extends Element> tsms = roundEnv.getElementsAnnotatedWith(annotationClass);


                for (Element element : tsms) {
                    for (AnnotationMirror am : element.getAnnotationMirrors()) {
                        if (am.getAnnotationType().toString().equals("io.virtdata.annotations.Service")) {
                            if(am.getElementValues().size()==1) {
                                AnnotationValue av = am.getElementValues().get("0");
                            } else {
                                messenger.printMessage(Diagnostic.Kind.ERROR, element.toString() + " should have one service interface name");
                            }
                        }
                    }
                }

                if (tsms.size() > 0) {

                    Writer w = getWriterForClass(annotationClass, tsms.toArray(new Element[0]));
                    for (Element e : tsms) {
                        w.write(((TypeElement) e).getQualifiedName() + "\n");
                    }
                }
            }

            for (Writer writer : this.writers.values()) {
                writer.close();
            }

        } catch (Exception e) {
            messenger.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
        return true;
    }
}
