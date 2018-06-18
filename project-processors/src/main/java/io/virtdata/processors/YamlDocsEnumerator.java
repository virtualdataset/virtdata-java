package io.virtdata.processors;

import org.yaml.snakeyaml.Yaml;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import javax.tools.StandardLocation;
import java.io.IOException;

public class YamlDocsEnumerator implements DocsEnumerator.Listener {

    private Filer filer;
    private Messager messenger;

    public YamlDocsEnumerator(Filer filer, Messager messenger) {

        this.filer = filer;
        this.messenger = messenger;
    }
    @Override
    public void onDocModel(DocModel m) {
        Yaml yamlAPI = new Yaml();
        String yaml = yamlAPI.dump(m);
        String packageName = m.getPackageName();

        try {
            filer.createResource(StandardLocation.SOURCE_OUTPUT,packageName,"RelativeName-" + m.getClassName());
        } catch (IOException e) {
            messenger.printMessage(Diagnostic.Kind.ERROR,e.toString());
        }
    }
}

