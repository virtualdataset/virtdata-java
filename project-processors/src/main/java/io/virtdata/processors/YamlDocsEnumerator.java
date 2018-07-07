package io.virtdata.processors;

import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class YamlDocsEnumerator implements FuncEnumerator.Listener {

    Yaml yamlAPI = new Yaml();
    private Filer filer;
    private Messager messenger;
    private String anchorPackage;
    private String anchorElement;
    private List<String> yamlDocs = new ArrayList<>();
    private Writer writer;


    public YamlDocsEnumerator(Filer filer, Messager messenger) {

        this.filer = filer;
        this.messenger = messenger;
    }

    @Override
    public void onAnchorModel(String packageName, String anchorName) {
        this.anchorPackage = packageName;
        this.anchorElement = anchorName;
    }

    @Override
    public void onFunctionModel(DocForFunc functionDoc) {
        String yaml = yamlAPI.dump(functionDoc);
        this.yamlDocs.add(yaml);
    }

    @Override
    public void onComplete() {
        Writer writer = getWriter();
        try {
            for (String yamlDoc : yamlDocs) {
                writer.write(yamlDoc);
                writer.write("---\n");
            }
        } catch (IOException e) {
            messenger.printMessage(Diagnostic.Kind.ERROR, e.toString());
        }
    }

    private Writer getWriter() {
        if (writer == null) {
            try {
                FileObject resource;
                messenger.printMessage(Diagnostic.Kind.NOTE, "writing docs manifest to " + anchorPackage + "." + anchorElement + "AutoDocs.yaml");
                resource = filer.createResource(StandardLocation.CLASS_OUTPUT, anchorPackage, anchorElement + "AutoDocs.yaml");
                writer = resource.openWriter();
            } catch (IOException e) {
                messenger.printMessage(Diagnostic.Kind.ERROR, e.toString());
            }
        }
        return writer;
    }
}

