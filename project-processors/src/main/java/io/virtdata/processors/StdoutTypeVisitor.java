package io.virtdata.processors;

import javax.lang.model.element.ElementVisitor;
import javax.lang.model.type.*;
import java.io.PrintStream;

public class StdoutTypeVisitor implements TypeVisitor<Void,Void> {

    private final PrintStream out;
    private ElementVisitor<?,?> ev = new StdoutElementVisitor();

    public StdoutTypeVisitor() {
        this.out = System.out;
    }

    @Override
    public Void visit(TypeMirror t, Void aVoid) {
        out.println("root TypeMirror: " + t);
        return null;
    }

    @Override
    public Void visit(TypeMirror t) {
        out.println("root TypeMirror: t");
        return null;
    }

    @Override
    public Void visitPrimitive(PrimitiveType t, Void aVoid) {
        out.println("primitive: " + t);
        return null;
    }

    @Override
    public Void visitNull(NullType t, Void aVoid) {
        out.println("null: " + t);
        return null;
    }

    @Override
    public Void visitArray(ArrayType t, Void aVoid) {
        out.println("array:" + t);
        return null;
    }

    @Override
    public Void visitDeclared(DeclaredType t, Void aVoid) {
        out.println("declared: " + t);
        t.asElement().accept(ev,null);
        return null;
    }

    @Override
    public Void visitError(ErrorType t, Void aVoid) {
        out.println("error: "+ t);
        return null;
    }

    @Override
    public Void visitTypeVariable(TypeVariable t, Void aVoid) {
        out.println("typeVariable: " + t);
        return null;
    }

    @Override
    public Void visitWildcard(WildcardType t, Void aVoid) {
        out.println("wildCard: " + t);
        return null;
    }

    @Override
    public Void visitExecutable(ExecutableType t, Void aVoid) {
        out.println("executable: " + t);
        return null;
    }

    @Override
    public Void visitNoType(NoType t, Void aVoid) {
        out.println("noType: "+ t);
        return null;
    }

    @Override
    public Void visitUnknown(TypeMirror t, Void aVoid) {
        out.println("unknown: " + t);
        return null;
    }

    @Override
    public Void visitUnion(UnionType t, Void aVoid) {
        out.println("union: " + t);
        return null;
    }

    @Override
    public Void visitIntersection(IntersectionType t, Void aVoid) {
        out.println("intersection: " + t);
        return null;
    }
}
