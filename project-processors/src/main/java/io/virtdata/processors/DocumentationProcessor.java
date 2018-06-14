package io.virtdata.processors;

import io.virtdata.annotations.PerThreadMapper;
import io.virtdata.annotations.ThreadSafeMapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SupportedOptions({"title"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"io.virtdata.annotations.ThreadSafeMapper", "io.virtdata.annotations.PerThreadMapper"})
public class DocumentationProcessor extends AbstractProcessor {


    private Filer filer;
    private Map<String, String> options;
    private Elements elementUtils;
    private Messager messenger;
    private SourceVersion sourceVersion;
    private Types typeUtils;

    private static Pattern packageNamePattern = Pattern.compile("(?<packageName>.+)?\\.(?<className>.+)");

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

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        DocsEnumerator enumerator = new DocsEnumerator(this.typeUtils, this.elementUtils, this.filer);
        enumerator.addListener(new DocsEnumerator.Listener() {
            @Override
            public void onDocModel(DocModel m) {
                System.out.println(m);
            }
        });

        enumerator.addListener(new YamlDocsEnumerator());

        List<Element> ts = new ArrayList<>();

        ts.addAll(roundEnv.getElementsAnnotatedWith(ThreadSafeMapper.class));
        ts.addAll(roundEnv.getElementsAnnotatedWith(PerThreadMapper.class));

        for(Element classElem: ts) {

            if (classElem.getKind()!= ElementKind.CLASS) {
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

            enumerator.onClass(packageName, simpleClassName, classDoc!=null? classDoc : "");

            for(Element ctorElem: classElem.getEnclosedElements()) {
                if (ctorElem.getKind() == ElementKind.METHOD) {
                    if (ctorElem.getSimpleName().toString().startsWith("apply")) {
                        VariableElement inParam = ((ExecutableElement) ctorElem).getParameters().get(0);
                        String inType = inParam.asType().toString();
                        String outType = ((ExecutableElement) ctorElem).getReturnType().toString();
                        enumerator.onApplyTypes(inType, outType);
                    }
                }
            }

            for(Element ctorElem: classElem.getEnclosedElements()) {
                if (ctorElem.getKind()==ElementKind.CONSTRUCTOR) {
                    // Args
                    List<? extends VariableElement> parameters = ((ExecutableElement) ctorElem).getParameters();
                    LinkedHashMap<String,String> args = new LinkedHashMap<>();
                    for (VariableElement parameter : parameters) {
                        args.put(parameter.getSimpleName().toString(),parameter.asType().toString());
                    }

                    // Javadoc
                    String ctorDoc = elementUtils.getDocComment(ctorElem);
                    ctorDoc = ctorDoc==null ? "" : ctorDoc;
                    enumerator.onInitializer(args,ctorDoc);
                }

            }

            enumerator.flush();
        }

        return false;
    }
}
