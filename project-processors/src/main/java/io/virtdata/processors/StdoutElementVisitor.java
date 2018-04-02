package io.virtdata.processors;

import javax.lang.model.element.*;
import java.io.PrintStream;
import java.util.List;

public class StdoutElementVisitor implements ElementVisitor<StringBuilder,StringBuilder> {

    private final PrintStream out;

    public StdoutElementVisitor() {
        this.out = System.out;
    }

    @Override
    public StringBuilder visit(Element e, StringBuilder sb) {
        sb.append("element: " + e +" type(" + e.asType() + ") kind(" + e.getKind() +")\n");

        if (e.getKind()== ElementKind.CONSTRUCTOR) {
            sb.append("constructor: " + e +"\n");
        }

        return sb;
    }

    @Override
    public StringBuilder visit(Element e) {
        throw new RuntimeException("This processor requires a StringBuilder as the second argument to visit.");
    }

    @Override
    public StringBuilder visitPackage(PackageElement e, StringBuilder sb) {
        sb.append("package: " + e);
        return sb;
    }

    @Override
    public StringBuilder visitType(TypeElement e, StringBuilder sb) {
        sb.append("typeElement: " + e +"\n");
        List<? extends Element> es = e.getEnclosedElements();
        for (Element element : es) {
            visit(element,sb);
        }
        return sb;
    }

    @Override
    public StringBuilder visitVariable(VariableElement e, StringBuilder sb) {
        sb.append("variable: " + e +"\n");
        return sb;
    }

    @Override
    public StringBuilder visitExecutable(ExecutableElement e, StringBuilder sb) {
        sb.append("executable: " + e +"\n");
        return sb;
    }

    @Override
    public StringBuilder visitTypeParameter(TypeParameterElement e, StringBuilder sb) {
        sb.append("typeParameter: " + e +"\n");
        return sb;
    }

    @Override
    public StringBuilder visitUnknown(Element e, StringBuilder sb) {
        sb.append("unknown:" + e +"\n");
        return sb;
    }
}
