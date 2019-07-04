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
    public Path getTemplatePath() {
        return sourcePath;
    }

    @Override
    public String getRawTemplate() {
        return RendererIO.readString(sourcePath);
    }

    public String toString() {
        return "source:" + sourcePath.toString() + " @" + getVersion() + "\n" + getRawTemplate() + "\n";
    }

}
