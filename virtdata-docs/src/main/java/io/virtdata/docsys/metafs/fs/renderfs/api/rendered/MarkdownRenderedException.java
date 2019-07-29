package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionData;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;

public class MarkdownRenderedException implements RenderedContent {

    private final Object[] details;
    private final Exception exception;
    private final TemplateView templateView;
    private final ViewModel viewModel;
    private final VersionData versions;

    public MarkdownRenderedException(Exception exception, TemplateView templateView, ViewModel viewModel, Object... details) {
        this.exception = exception;
        this.templateView = templateView;
        this.viewModel = viewModel;
        this.versions = new VersionData(templateView, viewModel);
        this.details = details;
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
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append("```\n");
        sb.append("# ERROR: ").append(exception.getMessage()).append("\n");
        sb.append("# Template:\n");
        sb.append(templateView.toString()).append("\n");
        sb.append("# View:\n");
        sb.append(viewModel.toString());
        sb.append("```\n");
        return sb.toString();
    }

}
