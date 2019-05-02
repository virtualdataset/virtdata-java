package io.virtdata.librealer;

import io.virtdata.autodoctypes.DocFuncData;
import io.virtdata.services.ModuleFunctionFinder;

import java.util.List;

public class LibraryFunctionFinder extends ModuleFunctionFinder {

    @Override
    public List<Path> getFunctionPaths() {
        return super.getFunctionPaths();
    }

    @Override
    public Module getModuleOfFinder() {
        return super.getModuleOfFinder();
    }

    @Override
    public List<? extends DocFuncData> getFuncDataForModule() {
        return super.getFuncDataForModule();
    }
}
