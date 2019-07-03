package io.virtdata.docsys.metafs.fs.renderfs.api.rendering;

import java.nio.file.Path;

public interface TemplateView extends Versioned {
    Path getTemplatePath();
    String getRawTemplate();


}
