package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Versioned;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;

public class HTMLRenderedException implements RenderedContent {

    private Versioned versionDependency;
    private final Object[] details;
    private final Exception exception;
    private final TemplateView templateView;
    private final ViewModel viewModel;

    public HTMLRenderedException(Exception exception, TemplateView templateView, ViewModel viewModel, Versioned versionDependency, Object... details) {
        this.exception = exception;
        this.templateView = templateView;
        this.viewModel = viewModel;
        this.versionDependency = versionDependency;
        this.details = details;
    }

    @Override
    public long getVersion() {
        return Math.max(viewModel.getVersion(),templateView.getVersion());
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

    @Override
    public Versioned getVersionDependency() {
        return versionDependency;
    }
}
