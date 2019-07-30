package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.Versioned;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionedPath;

import java.nio.file.Path;

public final class SourcePathTemplate implements TemplateView {

    private final Path sourcePath;
    private final Versioned versions;


    public SourcePathTemplate(Path sourcePath) {
        this.sourcePath = sourcePath;
        this.versions = new VersionedPath(sourcePath);
    }

    @Override
    public long getVersion() {
        return versions.getVersion();
    }

    @Override
    public boolean isValid() {
        return versions.isValid();
    }

    @Override
    public Path getTemplatePath() {
        return sourcePath;
    }

    @Override
    public String getRawTemplate() {
        try {
            return RendererIO.readString(sourcePath);
        } catch (Exception e) {
            throw e;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("source:").append(sourcePath.toString());
        sb.append(" @").append(getVersion());
        try {
            String rawTemplate = getRawTemplate();
            sb.append(" raw template:\n").append(rawTemplate).append("\n");
        } catch (Exception e) {
            sb.append("error rendering template: " + e.getMessage());
        }
        return sb.toString();
    }

}
