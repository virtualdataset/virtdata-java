package io.virtdata.processors;

import javax.lang.model.type.*;

public abstract class BaseDocVisitor implements TypeVisitor<String,Void> {

    @Override
    public String visit(TypeMirror t, Void aVoid) {
        return null;
    }

    @Override
    public String visit(TypeMirror t) {
        return null;
    }

    @Override
    public String visitPrimitive(PrimitiveType t, Void aVoid) {
        return null;
    }

    @Override
    public String visitNull(NullType t, Void aVoid) {
        return null;
    }

    @Override
    public String visitArray(ArrayType t, Void aVoid) {
        return null;
    }

    @Override
    public String visitDeclared(DeclaredType t, Void aVoid) {
        return null;
    }

    @Override
    public String visitError(ErrorType t, Void aVoid) {
        return null;
    }

    @Override
    public String visitTypeVariable(TypeVariable t, Void aVoid) {
        return null;
    }

    @Override
    public String visitWildcard(WildcardType t, Void aVoid) {
        return null;
    }

    @Override
    public String visitExecutable(ExecutableType t, Void aVoid) {
        return null;
    }

    @Override
    public String visitNoType(NoType t, Void aVoid) {
        return null;
    }

    @Override
    public String visitUnknown(TypeMirror t, Void aVoid) {
        return null;
    }

    @Override
    public String visitUnion(UnionType t, Void aVoid) {
        return null;
    }

    @Override
    public String visitIntersection(IntersectionType t, Void aVoid) {
        return null;
    }
}
