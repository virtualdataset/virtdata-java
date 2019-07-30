package io.virtdata.docsys.metafs.fs.renderfs.api.rendering;

import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.Versioned;

import java.nio.file.Path;

public interface TemplateView extends Versioned {
    Path getTemplatePath();
    String getRawTemplate();
}
