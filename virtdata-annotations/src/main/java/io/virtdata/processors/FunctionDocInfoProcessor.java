package io.virtdata.processors;

//import io.virtdata.annotations.*;

import io.virtdata.annotations.*;
import io.virtdata.autodoctypes.DocForFunc;
import io.virtdata.processors.internals.ClassNameListener;
import io.virtdata.processors.internals.FuncEnumerator;
import io.virtdata.processors.internals.FunctionDocInfoWriter;
import io.virtdata.processors.internals.ProcessorFileUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This documentation processor is responsible for finding all the enumerated that feed documentation
 * manifests. It simply calls listener interfaces to do the rest of the work.
 */
//@SupportedOptions({"title"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes({
        "io.virtdata.annotations.ThreadSafeMapper",
        "io.virtdata.annotations.PerThreadMapper"})
public class FunctionDocInfoProcessor extends AbstractProcessor {

    public final static String AUTOSUFFIX = "AutoDocsInfo";
    public static final String FUNCTIONS_FILE = "FUNCTION-DATA/functions.txt";

    private static Pattern packageNamePattern = Pattern.compile("(?<packageName>.+)?\\.(?<className>.+)");
    private static Pattern inheritDocPattern = Pattern.compile("(?ms)(?<pre>.*)(?<inherit>\\{@inheritDoc})(?<post>.*)$");
    private Filer filer;
    private Map<String, String> options;
    private Elements elementUtils;
    private Messager messenger;
    private SourceVersion sourceVersion;
    private Types typeUtils;
    private FuncEnumerator enumerator;
    private ClassNameListener classNameListener;
    private List<String> functionNames = new ArrayList<String>();
    private Set<ModuleElement> moduleElements = new HashSet<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.options = processingEnv.getOptions();
        this.elementUtils = processingEnv.getElementUtils();
        this.messenger = processingEnv.getMessager();
        this.sourceVersion = processingEnv.getSourceVersion();
        this.typeUtils = processingEnv.getTypeUtils();

        this.enumerator = new FuncEnumerator(this.typeUtils, this.elementUtils, this.filer);
        classNameListener = new ClassNameListener();
        enumerator.addListener(classNameListener);
        enumerator.addListener(new FunctionDocInfoWriter(this.filer, this.messenger, AUTOSUFFIX));

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        messenger.printMessage(Diagnostic.Kind.NOTE, "last round of annotation processing? " + roundEnv.processingOver());

        List<Element> ts = new ArrayList<>();


        Class<ThreadSafeMapper> tsmc = ThreadSafeMapper.class;
        Class<PerThreadMapper> ptmc = PerThreadMapper.class;
        ts.addAll(roundEnv.getElementsAnnotatedWith(tsmc));
        ts.addAll(roundEnv.getElementsAnnotatedWith(ptmc));

        for (Element classElem : ts) {

            if (classElem.getKind() != ElementKind.CLASS) {
                throw new RuntimeException("Unexpected kind of element: " + classElem.getKind() + " for " + classElem.toString());
            }

            // package and Class Name

            Name qualifiedName = ((TypeElement) classElem).getQualifiedName();
            try {
                ModuleElement me = ((ModuleElement) classElem.getEnclosingElement().getEnclosingElement());
                moduleElements.add(me);
            } catch (Exception e) {
                messenger.printMessage(Diagnostic.Kind.ERROR, "class " + qualifiedName + " must be contained in a JPMS module.");
                throw new RuntimeException("class " + qualifiedName + " must be contained in a JPMS module.");
            }


            Matcher pnm = packageNamePattern.matcher(qualifiedName);
            if (!pnm.matches()) {
                throw new RuntimeException("Unable to match qualified name for package and name: " + qualifiedName);
            }
            String packageName = pnm.group("packageName");
            String simpleClassName = pnm.group("className");

            // Class JavaDoc

            String classDoc = elementUtils.getDocComment(classElem);
            classDoc = classDoc == null ? "" : cleanJavadoc(classDoc);
            classDoc = inheritDocs(classDoc, classElem);

            enumerator.onClass(packageName, simpleClassName, classDoc);
            functionNames.add(packageName + "." + simpleClassName);

            Categories categoryAnnotation = classElem.getAnnotation(Categories.class);
            if (categoryAnnotation != null) {
                Category[] value = categoryAnnotation.value();
                enumerator.onCategories(value);
            }
            // apply method types

            boolean foundApply = false;
            Element applyMethodElem = null;
            Element applyInClassElem = classElem;
            while (applyMethodElem == null && applyInClassElem != null) {
                for (Element candidateApplyElem : applyInClassElem.getEnclosedElements()) {
                    if (candidateApplyElem.getKind() == ElementKind.METHOD) {
                        if (candidateApplyElem.getSimpleName().toString().startsWith("apply")) {
                            applyMethodElem = candidateApplyElem;
                            break;
                        }

                    }
                }
                if (applyMethodElem != null) {
                    break;
                }
                applyInClassElem = elementUtils.getTypeElement(((TypeElement) applyInClassElem).getSuperclass().toString());
            }
            if (applyMethodElem == null) {
                messenger.printMessage(Diagnostic.Kind.ERROR, "Unable to enumerate input and output types for " + simpleClassName);
                return false;
            }

            VariableElement inParam = ((ExecutableElement) applyMethodElem).getParameters().get(0);
            String inType = inParam.asType().toString();
            String outType = ((ExecutableElement) applyMethodElem).getReturnType().toString();
            enumerator.onApplyTypes(inType, outType);

            // Ctors

            for (Element ctorElem : classElem.getEnclosedElements()) {
                if (ctorElem.getKind() == ElementKind.CONSTRUCTOR) {

                    // Ctor Args
                    List<? extends VariableElement> parameters = ((ExecutableElement) ctorElem).getParameters();
                    LinkedHashMap<String, String> args = new LinkedHashMap<>();
                    boolean isVarArgs = ((ExecutableElement) ctorElem).isVarArgs();
                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement var = parameters.get(i);
                        String varName = var.getSimpleName().toString();
                        String varType = var.asType().toString() + (i == parameters.size() - 1 ? (isVarArgs ? "..." : "") : "");
                        args.put(varName, varType);
                    }

                    // Ctor Javadoc
                    String ctorDoc = elementUtils.getDocComment(ctorElem);
                    ctorDoc = ctorDoc == null ? "" : cleanJavadoc(ctorDoc);

                    // Examples
                    List<List<String>> exampleData = new ArrayList<>();
                    Example[] exampleAnnotations = ctorElem.getAnnotationsByType(Example.class);
                    for (Example example : exampleAnnotations) {
                        example.value();
                        exampleData.add(Arrays.asList(example.value()));
                    }

                    enumerator.onConstructor(args, ctorDoc, exampleData);
                }

            }

            enumerator.flush();
        }

        if (roundEnv.processingOver()) {
            String classNames = classNameListener.getClassNames().stream().collect(Collectors.joining(","));
            this.messenger.printMessage(Diagnostic.Kind.NOTE, "found " + classNameListener.getClassNames().size() + " classes.");

            if (moduleElements.size() != 1) {
                messenger.printMessage(Diagnostic.Kind.ERROR, "Must have exactly one module in this annotation processor," +
                        " found " +
                        moduleElements.stream()
                                .map(ModuleElement::getQualifiedName)
                                .map(String::valueOf)
                                .collect(Collectors.joining(",")));
            }

            String moduleName = moduleElements.stream().findFirst().map(ModuleElement::getQualifiedName).map(String::valueOf).orElseThrow();
            String moduleList = functionNames.stream().collect(Collectors.joining("\n"));
            ProcessorFileUtils.writeGeneratedFileOrThrow(filer, "", FUNCTIONS_FILE, moduleList);

            moduleElements.clear();
            functionNames.clear();
        }

        return false;
    }

    private String inheritDocs(String classDoc, Element classElem) {
        if (classDoc == null) {
            return null;
        }
        Matcher matcher = inheritDocPattern.matcher(classDoc);
        if (!matcher.matches()) {
            return classDoc;
        }
        StringBuilder docData = new StringBuilder();
        String pre = matcher.group("pre");
        String post = matcher.group("post");

        Optional<TypeElement> inheritFromElement = Optional.ofNullable(((TypeElement) classElem).getSuperclass())
                .map(String::valueOf)
                .map(elementUtils::getTypeElement);


        if (!inheritFromElement.isPresent()) {
            messenger.printMessage(Diagnostic.Kind.ERROR, "Element " + classElem.toString() + " has '{@inheritDoc}', but a superclass was not found.");
            return pre + "UNABLE TO FIND ELEMENT TO INHERIT DOCS FROM for " + classElem.toString() + " " + post;
        }
        TypeElement inheritFromType = inheritFromElement.get();
        String inheritedDocs = elementUtils.getDocComment(inheritFromType);
        if (inheritedDocs == null) {
            messenger.printMessage(Diagnostic.Kind.ERROR, "javadocs are missing on " + inheritFromElement.toString() + ", but "
                    + classElem.toString() + " is trying to inherit docs from it.");
            return pre + "UNABLE TO FIND INHERITED DOCS for " + classElem.toString() + " " + post;
        }

        if (inheritDocPattern.matcher(inheritedDocs).matches()) {
            return pre + inheritDocs(inheritedDocs, inheritFromType) + post;
        } else {
            return pre + inheritedDocs + post;
        }

    }

    private String cleanJavadoc(String ctorDoc) {
        return ctorDoc.replaceAll("(?m)^ ", "");
    }

    private static class StdoutListener implements FuncEnumerator.Listener {
        @Override
        public void onFunctionModel(DocForFunc functionDoc) {
            System.out.println(functionDoc);
        }
    }
}
