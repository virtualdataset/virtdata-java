package io.virtdata.docsys.metafs.fs.renderfs.model.properties;

import io.virtdata.docsys.metafs.fs.renderfs.api.MarkdownStringer;

import java.nio.file.Path;

public class PathView implements MarkdownStringer {

    private final Path path;

    public PathView(Path path) {
        this.path = path;
    }

    @Override
    public String asMarkdown() {
        return path.toString();
    }
}
