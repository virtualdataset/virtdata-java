package io.virtdata.docsapp;

import io.virtdata.api.EnhancedDocs;
import io.virtdata.api.VirtDataFunctionLibrary;
import io.virtdata.core.VirtDataLibraries;
import io.virtdata.processors.DocCtorData;
import io.virtdata.processors.DocFuncData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DocsApp {
    private final static Logger logger = LoggerFactory.getLogger(DocsApp.class);

    private Map<String,List<DocFuncData>> groupedDocs = new HashMap<>();

    public static void main(String[] args) {
        new DocsApp().invoke(args);
    }

    private void invoke(String[] args) {

        StringBuilder sb = new StringBuilder();
        VirtDataLibraries libs = VirtDataLibraries.get();
        Map<String, VirtDataFunctionLibrary> libraries = libs.getLibraries();
        for (VirtDataFunctionLibrary library : libraries.values()) {
            if (library instanceof EnhancedDocs) {
                saveEnhancedDocModel((EnhancedDocs)library);
            } else {
                writeBasicDocs(sb,library);
            }
        }
        writeEnhancedDocs(sb);
        System.out.println(sb.toString());

    }

    private StringBuilder writeBasicDocs(StringBuilder sb, VirtDataFunctionLibrary library) {
        List<String> dataMapperNames = library.getDataMapperNames();

        for (String dataMapperName : dataMapperNames) {
            sb.append("## ").append(dataMapperName).append("\n");
        }

        return sb;

    }

    private void saveEnhancedDocModel(EnhancedDocs library) {
        for (DocFuncData docm : library.getDocModels()) {
            List<DocFuncData> group = this.groupedDocs.getOrDefault(docm.getClassName(), new ArrayList<>());
            group.add(docm);
            groupedDocs.put(docm.getClassName(),group);
        }
    }

    private StringBuilder writeEnhancedDocs(StringBuilder sb) {
        ArrayList<String> names = new ArrayList<>(groupedDocs.keySet());
        Collections.sort(names);

        for (String name : names) {
            List<DocFuncData> docs = groupedDocs.get(name);

            sb.append("## ").append(name).append("\n");

            List<DocFuncData> classdocs = docs.stream().filter(d -> d.getClassJavadoc() != null).collect(Collectors.toList());
            List<String> distinctClassDocs = classdocs.stream()
                    .map(DocFuncData::getClassJavadoc)
                    .map(String::trim)
                    .distinct()
                    .collect(Collectors.toList());

            if (distinctClassDocs.size()==0) {
                logger.warn("There were no class docs found for types named " + name );
            }
            if (distinctClassDocs.size()>1) {
                logger.warn("There were multiple class docs found for types named " + name );
            }

            if (distinctClassDocs.size()==1) {
                sb.append("\n").append(distinctClassDocs.get(0)).append("\n");
            }

            sb.append("\navailable forms:\n\n");
            for (DocFuncData doc : docs) {
                List<DocCtorData> ctors = doc.getCtors();
                for (DocCtorData ctor : ctors) {
                    sb.append("- ").append(doc.getInType()).append(" `->` ");
                    sb.append(doc.getClassName());
                    sb.append("(");
                    sb.append(
                        ctor.getArgs().entrySet().stream().map(
                                e -> e.getValue()+": "+e.getKey()
                        ).collect(Collectors.joining(", "))
                    );
                    sb.append(")");
                    sb.append(" `->` ").append(doc.getOutType()).append("\n");
                }
            }
            sb.append("\n");
        }
        return sb;
    }


}
