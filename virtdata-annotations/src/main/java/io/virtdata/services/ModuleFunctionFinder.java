package io.virtdata.services;

import io.virtdata.autodoctypes.DocFuncData;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModuleFunctionFinder implements FunctionFinderService {

    public Module getModuleOfFinder() {
        return this.getClass().getModule();
    }


    @Override
    public List<FunctionFinderService.Path> getFunctionPaths() {
        try {

            Module module = getModuleOfFinder();

            ResolvedModule thisModuleResolved = module.getLayer()
                    .configuration()
                    .findModule(module.getName())
                    .orElseThrow();

            ModuleReader moduleReader = thisModuleResolved.reference().open();
            InputStream funcfile = moduleReader.open(FunctionFinderService.FUNCTIONS_FILE).orElse(null);
            if (funcfile == null) {
                throw new RuntimeException(this.getClass().getCanonicalName() + " unable to open " + FunctionFinderService.FUNCTIONS_FILE
                        + " from a module that has a DocFuncDataFinder provided, module:" + getModuleOfFinder().getName());
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(funcfile));

            List<FunctionFinderService.Path> names = reader.lines()
                    .filter(Objects::nonNull)
                    .filter(s -> !s.isEmpty())
                    .map(String::trim)
                    .distinct()
                    .sorted()
                    .map(funcName -> new FunctionFinderService.Path(module.getName(), funcName, this))
                    .collect(Collectors.toList());

            return names;
        } catch (Exception e) {
            throw new RuntimeException("error while resolving function names: " + e.getMessage(), e);
        }
    }


    @Override
    public List<? extends DocFuncData> getFuncDataForModule() {
        List<FunctionFinderService.Path> funcNamesForModule = this.getFunctionPaths();
        List<DocFuncData> docFuncDataList = new ArrayList<>();

        return funcNamesForModule.stream().map(f -> f.instantiateDocs()).collect(Collectors.toList());

//        try {
//            for (Path path : funcNamesForModule) {
//                Module thisModule = this.getClass().getModule();
//                if (path.moduleName.equals(thisModule.getName())) {
//                    Class<? extends DocFuncData> aClass = Class.forName(thisModule, path.className+"AutoDocsInfo")
//                            .asSubclass(DocFuncData.class);
//                    DocFuncData docFuncData = aClass.getConstructor().newInstance();
//                    docFuncDataList.add(docFuncData);
//                } else {
//                    throw new RuntimeException("the path module name '" + path.moduleName + "' is " +
//                            "not the same as the instantiating module '" + thisModule.getName());
//                }
//            }
//            return docFuncDataList;
//        } catch (Exception e) {
//            throw new RuntimeException("Error while getting function metadata: " + e.getMessage(), e);
//        }

    }

    public String toString() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        int[] elems = new int[]{2};
        for (int elem : elems) {
            StackTraceElement ste = stacktrace[elem];
//            sb.append(" E:").append(elem);
//            sb.append(" cls:").append(ste.getClassName());
//            sb.append(" mtd:").append(ste.getMethodName());
            sb.append(" (from:").append(ste.getModuleName()).append(")");
//            sb.append(" findermodule:").append(getModuleOfFinder());
        }
        return this.getClass().getModule().getName() + "/" + this.getClass().getSimpleName() + sb.toString();

    }
}
