package io.virtdata.processors;

import io.virtdata.annotations.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This documentation processor is responsible for finding all the enumerated that feed documentation
 * manifests. It simply calls listener interfaces to do the rest of the work.
 */
@SupportedOptions({"title"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "io.virtdata.annotations.DocManifestAnchor",
        "io.virtdata.annotations.ThreadSafeMapper",
        "io.virtdata.annotations.PerThreadMapper"})
public class DocumentationProcessor extends AbstractProcessor {

    private static Pattern packageNamePattern = Pattern.compile("(?<packageName>.+)?\\.(?<className>.+)");
    private Filer filer;
    private Map<String, String> options;
    private Elements elementUtils;
    private Messager messenger;
    private SourceVersion sourceVersion;
    private Types typeUtils;
    private String anchorPackage;
    private String anchorSimpleName;
    private DocsEnumerator enumerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.options = processingEnv.getOptions();
        this.elementUtils = processingEnv.getElementUtils();
        this.messenger = processingEnv.getMessager();
        this.sourceVersion = processingEnv.getSourceVersion();
        this.typeUtils = processingEnv.getTypeUtils();

        this.enumerator = new DocsEnumerator(this.typeUtils, this.elementUtils, this.filer);
//        enumerator.addListener(new StdoutListener());
//        enumerator.addListener(new YamlDocsEnumerator(this.filer, this.messenger));
        enumerator.addListener(new InlineDocData(this.filer,this.messenger,"AutoDocsInfo"));

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (this.anchorPackage==null) {
            messenger.printMessage(Diagnostic.Kind.WARNING, "Looking for manifest anchor in " + roundEnv.getRootElements().toString());
            findAnchor(roundEnv, enumerator);
        }

        List<Element> ts = new ArrayList<>();

        ts.addAll(roundEnv.getElementsAnnotatedWith(ThreadSafeMapper.class));
        ts.addAll(roundEnv.getElementsAnnotatedWith(PerThreadMapper.class));

        for (Element classElem : ts) {

            if (classElem.getKind() != ElementKind.CLASS) {
                throw new RuntimeException("Unexpected kind of element: " + classElem.getKind() + " for " + classElem.toString());
            }

            Name qualifiedName = ((TypeElement) classElem).getQualifiedName();
            Matcher pnm = packageNamePattern.matcher(qualifiedName);
            if (!pnm.matches()) {
                throw new RuntimeException("Unable to match qualified name for package and name: " + qualifiedName);
            }
            String packageName = pnm.group("packageName");
            String simpleClassName = pnm.group("className");
            String classDoc = elementUtils.getDocComment(classElem);
            classDoc = classDoc==null ? "" : cleanJavadoc(classDoc);

            enumerator.onClass(packageName, simpleClassName, classDoc);

            for (Element ctorElem : classElem.getEnclosedElements()) {
                if (ctorElem.getKind() == ElementKind.METHOD) {
                    if (ctorElem.getSimpleName().toString().startsWith("apply")) {
                        VariableElement inParam = ((ExecutableElement) ctorElem).getParameters().get(0);
                        String inType = inParam.asType().toString();
                        String outType = ((ExecutableElement) ctorElem).getReturnType().toString();
                        enumerator.onApplyTypes(inType, outType);
                    }
                }
            }

            for (Element ctorElem : classElem.getEnclosedElements()) {
                if (ctorElem.getKind() == ElementKind.CONSTRUCTOR) {


                    // Args
                    List<? extends VariableElement> parameters = ((ExecutableElement) ctorElem).getParameters();
                    LinkedHashMap<String, String> args = new LinkedHashMap<>();
                    boolean isVarArgs = ((ExecutableElement) ctorElem).isVarArgs();
                    for (int i = 0; i < parameters.size(); i++) {
                        VariableElement var = parameters.get(i);
                        String varName = var.getSimpleName().toString();
                        String varType = var.asType().toString() + (i==parameters.size()-1 ? (isVarArgs ? "..." : "") : "") ;
                        args.put(varName,varType);
                    }

                    // Javadoc
                    String ctorDoc = elementUtils.getDocComment(ctorElem);
                    ctorDoc = ctorDoc == null ? "" : cleanJavadoc(ctorDoc);

//                    // Examples
//                    Examples[] examples = ctorElem.getAnnotationsByType(Examples.class);
//                    for (Examples example : examples) {
//                        Example[] exampleEntry= example.value();
//                        String[] value = exampleEntry[0].value();
//                        System.out.println(value.toString());
//                    }

                    // Examples
                    List<List<String>> exampleData = new ArrayList<>();
                    Example[] exampleAnnotations = ctorElem.getAnnotationsByType(Example.class);
                    for (Example example : exampleAnnotations) {
                        exampleData.add(Arrays.asList(example.value()));
                    }

                    enumerator.onConstructor(args, ctorDoc, exampleData);
                }

            }

            enumerator.flush();
        }

        if (roundEnv.processingOver()) {
            if (anchorPackage==null) {
                messenger.printMessage(Diagnostic.Kind.ERROR, "There should be exactly one element in this library which has an " +
                        "annotation of type @" + DocManifestAnchor.class.getSimpleName() + ". Found none.");
                return false;
            }
            enumerator.onAnchor(anchorPackage, anchorSimpleName);
            enumerator.onComplete();
        }

        return false;
    }

    private String cleanJavadoc(String ctorDoc) {
        return ctorDoc.replaceAll("(?m)^ ","");
    }

    private boolean findAnchor(RoundEnvironment roundEnv, DocsEnumerator enumerator) {
        Set<? extends Element> anchors = roundEnv.getElementsAnnotatedWith(DocManifestAnchor.class);
        if (anchors.size() >1) {
            messenger.printMessage(Diagnostic.Kind.ERROR, "Found " + anchors.size() + " @DocManifestAnchor classes, expected 1.");
        }
        if (anchors.size()==0) {
            return false;
        }
        Element anchor = new ArrayList<>(anchors).get(0);
        Name anchorElement = ((TypeElement) anchor).getQualifiedName();
        Matcher pnm = packageNamePattern.matcher(anchorElement);
        if (!pnm.matches()) {
            messenger.printMessage(Diagnostic.Kind.ERROR, "Unable to match qualified name for package and name: " + anchorElement);
        }

        String anchorPackage = pnm.group("packageName");
        String anchorSimpleName = pnm.group("className");
        this.anchorPackage = anchorPackage;
        this.anchorSimpleName=anchorSimpleName;

        return true;
    }

    private static class StdoutListener implements DocsEnumerator.Listener {
        @Override
        public void onAnchorModel(String packageName, String anchorName) {
            System.out.println("anchor: " + packageName + " . " + anchorName);
        }

        @Override
        public void onFunctionModel(DocForFunc functionDoc) {
            System.out.println(functionDoc);
        }

        @Override
        public void onComplete() {
            System.out.println("complete");

        }
    }
}
