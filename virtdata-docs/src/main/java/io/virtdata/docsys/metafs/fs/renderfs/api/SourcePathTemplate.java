package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;

import java.nio.file.Path;

public final class SourcePathTemplate implements TemplateView {

    private final Path sourcePath;

    public SourcePathTemplate(Path sourcePath) {
        this.sourcePath = sourcePath;
    }

    @Override
    public long getVersion() {
        return RendererIO.mtimeFor(sourcePath);
    }

    @Override
    public String get() {
        return RendererIO.readString(sourcePath);
    }
}
