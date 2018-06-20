package io.virtdata.apps.docsapp;

import io.virtdata.api.EnhancedDocs;
import io.virtdata.api.VirtDataFunctionLibrary;
import io.virtdata.core.VirtDataLibraries;
import io.virtdata.processors.DocCtorData;
import io.virtdata.processors.DocFuncData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public class DocsApp {
    private final static Logger logger = LoggerFactory.getLogger(DocsApp.class);

    private Map<String,List<DocFuncData>> groupedDocs = new HashMap<>();
    private String output;
    private String print ="all";

    public static void main(String[] args) {
        new DocsApp().invoke(args);
    }

    private void invoke(String[] args) {
        LinkedList<String> largs = new LinkedList<>(Arrays.asList(args));
        while (largs.peekFirst()!=null) {
            String argtype = largs.removeFirst();
            if (largs.peekFirst()==null) {
                throw new RuntimeException(DocsApp.class.toString() + " expects args in param value couplets.");
            }
            String argval = largs.removeFirst().toLowerCase();
            switch(argtype) {
                case "output": this.output=argval;
                break;
                case "print":
                    if (argval.equals("all")||argval.equals("logs")) {
                        this.print =argval;
                    } else {
                        throw new InvalidParameterException("valid args for print: print all, print logs");
                    }
                default:
            }
        }

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
        String docdata = sb.toString();
        docdata = docdata.replaceAll("java.lang.","");
        docdata = docdata.replaceAll("\\s*</?pre>\\s*\n","\n```\n");
        docdata = docdata.replaceAll("(<p>|</p>| \n)+", "\n");
        docdata = docdata.replaceAll("<pre>","`").replaceAll("</pre>","`");
        docdata = docdata.replaceAll("\\{@link (.+?)}", "$1");
        docdata = docdata.replaceAll("(?m)@param .*\n","");
        docdata = docdata.replaceAll("(?m)\n\n+","\n\n");

        if (output!=null) {
            try {
                FileWriter writer = new FileWriter(output);
                writer.write(docdata);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (print.equals("all")) {
                System.out.println(docdata);
            } else if (print.equals("logs")) {
            } else {
                throw new RuntimeException("Undefined value for print:" + print);
            }
        }

    }

    private StringBuilder writeBasicDocs(StringBuilder sb, VirtDataFunctionLibrary library) {
        List<String> dataMapperNames = library.getDataMapperNames();

        for (String dataMapperName : dataMapperNames) {
//            sb.append("## ").append(dataMapperName).append("\n");
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

            sb.append("## ").append(name).append("\n\n");

            List<DocFuncData> classdocs = docs.stream()
                    .filter(d -> d.getClassJavadoc() != null && !d.getClassJavadoc().isEmpty())
                    .collect(Collectors.toList());

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
                String classdoc = distinctClassDocs.get(0);
                sb.append(classdoc);
                if (!classdoc.endsWith("\n\n")) {
                    sb.append("\n");
                }
                if (!classdoc.endsWith("\n")) {
                    sb.append("\n");
                }
            }

            for (DocFuncData doc : docs) {
                List<DocCtorData> ctors = doc.getCtors();
                for (DocCtorData ctor : ctors) {
                    sb.append("- ").append(doc.getInType()).append(" -> ");
                    sb.append(doc.getClassName());
                    sb.append("(");
                    sb.append(
                            ctor.getArgs().entrySet().stream().map(
                                    e -> e.getValue()+": "+e.getKey()
                            ).collect(Collectors.joining(", "))
                    );
                    sb.append(")");
                    sb.append(" -> ").append(doc.getOutType()).append("\n");
                    String ctorDoc = ctor.getCtorJavaDoc();
                    if (!ctorDoc.isEmpty()) {
                        sb.append("  - *notes:* ").append(ctorDoc);
                    }
                    for (List<String> example : ctor.getExamples()) {
                        sb.append("  - *ex:* `" + example.get(0) + "`");
                        if (example.size()>1) {
                            sb.append(" - *").append(example.get(1)).append("*");
                        }
                        sb.append("\n");
                    }
                }
            }
            sb.append("\n");


            sb.append("\n");
        }
        return sb;
    }


}
