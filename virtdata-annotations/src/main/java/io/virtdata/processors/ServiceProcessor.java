package io.virtdata.processors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    //    private Map<String, Writer> writers = new HashMap<>();
    private Map<String, Set<Element>> types = new HashMap<>();
    //private Set<String> classNames= new HashSet<>();
    private Set<ModuleElement> moduleElements = new HashSet<ModuleElement>();

//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        Set<String> supportedAnnotations = new HashSet<>();
//        supportedAnnotations.add("io.virtdata.annotations.Service");
//        return supportedAnnotations;
//    }

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
//                    if (element.getKind()== ElementKind.MODULE) {
//                        ModuleElement moduleElement = (ModuleElement) element;
//                        moduleElements.add(moduleElement);
//                    }
//                    if (element.getAnnotation(Service.class)==null) {
//                        continue;
//                    }

                    String serviceClass = null;
                    for (AnnotationMirror am : element.getAnnotationMirrors()) {
                        DeclaredType atype = am.getAnnotationType();
                        if (!annotationType.equals(atype.toString())) {
                            continue;
                        }

                        List<? extends ExecutableElement> valueKeys = am.getElementValues().keySet().stream()
                                .filter(k -> k.toString().equals("value()")).collect(Collectors.toList());
                        if (valueKeys.size() == 0) {
                            messenger.printMessage(Diagnostic.Kind.ERROR, "Annotation missing required value");
                            return false;
                        }
                        AnnotationValue annotationValue = am.getElementValues().get(valueKeys.get(0));
                        serviceClass = annotationValue.getValue().toString();

                        Set<Element> typeSet = this.types.computeIfAbsent(serviceClass, s -> new HashSet<Element>());

                        TypeElement typeElem = (TypeElement) element;
                        typeSet.add(typeElem);

                        ModuleElement moduleElem = ((ModuleElement) typeElem.getEnclosingElement().getEnclosingElement());
                        this.moduleElements.add(moduleElem);

                        messenger.printMessage(Diagnostic.Kind.NOTE,
                                "Recording service entry for implementation of "
                                        + serviceClass + ": " + typeElem.toString());
                    }


                }
            }


            if (roundEnv.processingOver()) {
//                FileObject moduleInfo = openModuleInfo();
//                if (moduleInfo != null) {
//                    writeJPMSServices(moduleInfo);
//                } else {
                writeMetaInfServices();
//                }
            }


        } catch (Exception e) {
            messenger.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
        return true;
    }

    private void writeJPMSServices(FileObject extantModuleInfo) {
        try {
            Reader reader = extantModuleInfo.openReader(false);
            StringWriter extantInfo = new StringWriter();
            reader.transferTo(extantInfo);
            String moduleInfo = extantInfo.getBuffer().toString();
            String jpmsEntries = getJPMSEntries();
            String newModuleInfo = moduleInfo.replaceFirst("// AUTOSERVICES *\n", jpmsEntries);
            if (moduleInfo.equals(newModuleInfo)) {
                messenger.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "No changes to module info file.");
            } else {
                FileObject toWrite = filer.createResource(StandardLocation.SOURCE_OUTPUT, "", "module-info.java");
                Writer moduleWriter = toWrite.openWriter();
                moduleWriter.write(newModuleInfo);
                moduleWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getJPMSEntries() {

        StringBuilder sb = new StringBuilder();
        for (String serviceType : types.keySet()) {
            sb.append("// Added by annotation processor " + this.getClass().getCanonicalName()).append("\n");
            sb.append("  provides " + serviceType + " with");
            for (Element element : types.get(serviceType)) {
                Name name = ((TypeElement) element).getQualifiedName();
                sb.append("\n   ").append(name.toString()).append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append(";\n");
        }
        return sb.toString();
    }

    private FileObject openModuleInfo() {

        if (moduleElements.size() != 1) {
            messenger.printMessage(Diagnostic.Kind.ERROR, "found more or less than one module: " + moduleElements
                    .stream().map(ModuleElement::getQualifiedName).collect(Collectors.joining(",")));
            throw new RuntimeException("Unable to continue");
        }
        String moduleName = moduleElements.stream().findFirst().map(ModuleElement::getQualifiedName).map(String::valueOf).orElseThrow();

        try {
            FileObject resource = filer.getResource(
                    StandardLocation.SOURCE_PATH,
                    "",
                    "module-info.java"
            );
            messenger.printMessage(Diagnostic.Kind.OTHER, "This module IS JPMS modular, adding services.");
            return resource;
        } catch (IOException ignored) {
            messenger.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "This module is not JPMS modular, falling back to classpath + META-INF services");
            return null;
        }
    }

    private void writeMetaInfServices() throws IOException {
        for (String serviceType : types.keySet()) {
            messenger.printMessage(Diagnostic.Kind.MANDATORY_WARNING, "Writing services file for " + serviceType);
            Set<Element> tsms = types.get(serviceType);
            Element[] elements = new ArrayList<>(tsms).toArray(new Element[0]);
            FileObject servicesFile = filer.createResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    "META-INF/services/" + serviceType,
                    elements);
            Writer w = servicesFile.openWriter();
            for (Element tsm : tsms) {
                Name className = ((TypeElement) tsm).getQualifiedName();
                w.write(className.toString() + "\n");
            }
            w.close();
        }
    }
}
