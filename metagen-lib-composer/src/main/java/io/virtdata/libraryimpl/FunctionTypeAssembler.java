package io.virtdata.libraryimpl;

import io.virtdata.api.FunctionType;

public enum FunctionTypeAssembler {
    long_long_assembler (FunctionType.long_long,ComposerForLongUnaryOperator.class);

    private final FunctionType ft;
    private final Class<? extends FunctionComposer> fas;

    FunctionTypeAssembler(FunctionType ft, Class<? extends FunctionComposer> fasClass) {
        this.ft = ft;
        this.fas = fasClass;
    }

    public FunctionType getFunctionType() {
        return ft;
    }

}
