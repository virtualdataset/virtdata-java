package io.virtdata.processors;

import org.yaml.snakeyaml.Yaml;

public class YamlDocsEnumerator implements DocsEnumerator.Listener {

    @Override
    public void onDocModel(DocModel m) {
        Yaml yamlAPI = new Yaml();
        String yaml = yamlAPI.dump(m);
    }
}

