package io.virtdata.docsys.metafs.fs.renderfs.model.properties;

import io.virtdata.docsys.metafs.fs.renderfs.api.MarkdownStringer;

import java.util.ArrayList;
import java.util.Collection;

public class ListView extends ArrayList<String> implements MarkdownStringer {

    public ListView(Collection<? extends String> c) {
        super(c);
    }

    @Override
    public String asMarkdown() {
        StringBuilder sb = new StringBuilder();
        for (String value : this) {
            sb.append("- ").append(value).append("\n");
        }
        return sb.toString();
    }
}
