package io.virtdata.processors;

import com.squareup.javapoet.*;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InlineDocData implements DocsEnumerator.Listener {

    private final String suffix;
    Yaml yamlAPI = new Yaml();
    private Filer filer;
    private Messager messenger;
    private String anchorPackage;
    private String anchorElement;
    private List<DocForFunc> docs = new ArrayList<>();
    private Writer writer;

    public InlineDocData(Filer filer, Messager messenger, String suffix) {
        this.filer = filer;
        this.messenger = messenger;
        this.suffix = suffix;
    }

    @Override
    public void onAnchorModel(String packageName, String anchorName) {
        this.anchorPackage = packageName;
        this.anchorElement = anchorName;
    }

    @Override
    public void onFunctionModel(DocForFunc functionDoc) {
        docs.add(functionDoc);
    }

    @Override
    public void onComplete() {

        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("getDocsInfo")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(List.class, DocFuncData.class));

        CodeBlock preamble = CodeBlock.builder()
                .addStatement("$T<$T> docFuncData = new $T<$T>();", List.class, DocFuncData.class, ArrayList.class, DocFuncData.class)
                .build();

        CodeBlock.Builder bigmethod = CodeBlock.builder().add(preamble);

        //        .addStatement(preamble).addStatement("$>");

        for (DocForFunc doc : docs) {
            bigmethod.add("docFuncData.add(\n$>new $T(){{\n$>", DocForFunc.class);
            bigmethod.addStatement("setClassName($S)", doc.getClassName());
            bigmethod.addStatement("setPackageName($S)", doc.getPackageName());
            bigmethod.addStatement("setClassJavadoc($S)", doc.getClassJavadoc());
            bigmethod.addStatement("setInType($S)", doc.getInType());
            bigmethod.addStatement("setOutType($S)", doc.getOutType());
            for (DocCtorData ctor : doc.getCtors()) {
                bigmethod.add("addCtor($S,\n$>",ctor.getCtorJavaDoc());
                bigmethod.add("new $T<String,String>() {{\n$>", LinkedHashMap.class);
                for (Map.Entry<String, String> arg : ctor.getArgs().entrySet()) {
                    bigmethod.add("put($S,$S);\n", arg.getKey(), arg.getValue());
                }
                bigmethod.add("$<}},\n");
                bigmethod.add("new $T<$T<$T>>() {{$>\n", ArrayList.class, List.class,String.class);
                for (List<String> examples : ctor.getExamples()) {
                    bigmethod.add("add(new $T<$T>() {{$>\n", ArrayList.class, String.class);
                    for (String example : examples) {
                        bigmethod.add("add($S);\n",example);
                    }
                    bigmethod.add("$<}});\n");
                }
                bigmethod.add("$<}}\n$<);\n");
            }
            bigmethod.add("$<}}\n$<);\n");

        }
        bigmethod.add("return docFuncData;\n");
        CodeBlock bigCodeBlock = bigmethod.build();
        MethodSpec bigMethod = methodSpec.addCode(bigCodeBlock).build();

        TypeSpec manifestType = TypeSpec.classBuilder(anchorElement + suffix)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(bigMethod)
                .addSuperinterface(VirtDataLibraryInfo.class)
                .build();

        JavaFile javafile = JavaFile.builder(this.anchorPackage, manifestType)
                .addFileComment("This file is auto-generated.")
                .build();

        try {
            javafile.writeTo(this.filer);
        } catch (IOException e) {
            messenger.printMessage(Diagnostic.Kind.ERROR, "Error writing javafile " + anchorElement + suffix + " in package " + this.anchorPackage);
            throw new RuntimeException(e);
        }

    }


    private TypeSpec createInlineClassForDocFuncData(DocForFunc doc) {
        List<MethodSpec> methods = new ArrayList<>();

        MethodSpec getClassName = MethodSpec.methodBuilder("getClassName")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $S", doc.getClassName())
                .build();
        methods.add(getClassName);

        MethodSpec getPackageName = MethodSpec.methodBuilder("getPackageName")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $S", doc.getPackageName())
                .build();

        methods.add(getPackageName);

        MethodSpec getClassJavaDoc = MethodSpec.methodBuilder("getClassJavaDoc")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $S", doc.getClassJavadoc())
                .build();
        methods.add(getClassJavaDoc);

        MethodSpec getInType = MethodSpec.methodBuilder("getInType")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $S", doc.getInType())
                .build();
        methods.add(getInType);

        MethodSpec getOutType = MethodSpec.methodBuilder("getOutType")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $S", doc.getOutType())
                .build();
        methods.add(getOutType);

        CodeBlock ctorsHead = CodeBlock.builder().add("return new $T<$T>() {{$>\n", ArrayList.class, DocForFuncCtor.class).build();
        CodeBlock ctorsTail = CodeBlock.builder().add("$<};").build();

        CodeBlock.Builder ctors = CodeBlock.builder().add(ctorsHead);
        for (DocCtorData ctor : doc.getCtors()) {
            ctors.add("add(new DocForFuncCtor($S, $S, \n$>new $T<String, String>() {{\n$>", ctor.getClassName(), ctor.getCtorJavaDoc(), LinkedHashMap.class);
            for (Map.Entry<String, String> arg : ctor.getArgs().entrySet()) {
                ctors.add("put($S,$S);\n", arg.getKey(), arg.getValue());
            }
            ctors.add("$<}}\n$<));\n");
        }
        ctors.add(ctorsTail);

        MethodSpec getCtors = MethodSpec.methodBuilder("getCtors")
                .addModifiers(Modifier.PUBLIC)
                .returns(List.class)
                .addStatement(ctors.build())
                .build();
        methods.add(getCtors);

        TypeSpec inlineSpec = TypeSpec.classBuilder(this.anchorElement + suffix)
                .addMethods(methods)
                .build();

        return inlineSpec;
    }

    private Writer getWriter() {
        if (writer == null) {
            try {
                FileObject resource;
                String docDataFilename = anchorPackage + "." + anchorElement + suffix;
                messenger.printMessage(Diagnostic.Kind.NOTE, "writing docs manifest to " + docDataFilename);
                JavaFileObject sourceFile = filer.createSourceFile(anchorPackage + "." + anchorElement + "DocData");

            } catch (IOException e) {
                messenger.printMessage(Diagnostic.Kind.ERROR, e.toString());
            }
        }
        return writer;
    }
}

