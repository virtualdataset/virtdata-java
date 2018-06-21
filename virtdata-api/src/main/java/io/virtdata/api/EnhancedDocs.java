package io.virtdata.api;

import io.virtdata.processors.DocFuncData;

import java.util.List;
import java.util.stream.Collectors;

public interface EnhancedDocs {
    List<DocFuncData> getDocModels();

    default List<String> getDocPackages() {
        List<DocFuncData> docModels = getDocModels();
        List<String> packages = docModels.stream().map(DocFuncData::getPackageName).distinct().collect(Collectors.toList());
        return packages;
    }
}
