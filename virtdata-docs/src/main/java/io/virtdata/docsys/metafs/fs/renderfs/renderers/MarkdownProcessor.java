package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.*;

public class MarkdownProcessor implements TemplateCompiler {

    @Override
    public Renderer apply(TemplateView templateView) {
        return new MarkdownRenderer(templateView);
    }

    @Override
    public String toString() {
        return "\uD83C\uDD6B";
//        return MarkdownProcessor.class.getSimpleName();
    }

}
