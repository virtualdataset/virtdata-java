package io.virtdata.processors;

import com.squareup.javapoet.*;

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

public class SingleInlineDocData implements FuncEnumerator.Listener {

    private final String suffix;
    private Filer filer;
    private Messager messenger;
    private List<DocForFunc> docs = new ArrayList<>();

    public SingleInlineDocData(Filer filer, Messager messenger, String suffix) {
        this.filer = filer;
        this.messenger = messenger;
        this.suffix = suffix;
    }

    @Override
    public void onFunctionModel(DocForFunc functionDoc) {

        TypeSpec typeSpec = this.createInlineClassForDocFuncData(functionDoc,
                functionDoc.getClassName()+suffix);

        JavaFile javafile = JavaFile.builder(functionDoc.getPackageName(), typeSpec)
                .addFileComment("This file is auto-generated.")
                .build();

        try {
            javafile.writeTo(this.filer);
        } catch (IOException e) {
            messenger.printMessage(Diagnostic.Kind.ERROR, "Error writing javafile " + anchorElement + suffix + " in package " + this.anchorPackage);
            throw new RuntimeException(e);
        }

    }

    private TypeSpec createInlineClassForDocFuncData(DocForFunc doc, String newClassName) {
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

        MethodSpec getCtorsMethod = MethodSpec.methodBuilder("getCtors")
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(List.class,DocForFuncCtor.class))
                .addStatement(ctors.build())
                .build();
        methods.add(getCtorsMethod);


        TypeSpec manifestType = TypeSpec.classBuilder(newClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methods)
                .addSuperinterface(DocFuncData.class)
                .build();

        return manifestType;
    }
}

