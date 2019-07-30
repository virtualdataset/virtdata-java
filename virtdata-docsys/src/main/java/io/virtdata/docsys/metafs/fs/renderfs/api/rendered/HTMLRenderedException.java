package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionData;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;

import java.io.PrintWriter;
import java.io.StringWriter;

public class HTMLRenderedException implements RenderedContent<String> {

    private final Object[] details;
    private final Exception exception;
    private final TemplateView templateView;
    private final ViewModel viewModel;
    private final VersionData versions;

    public HTMLRenderedException(Exception exception, TemplateView templateView, ViewModel viewModel, Object... details) {
        this.exception = exception;
        this.templateView = templateView;
        this.viewModel = viewModel;
        this.details = details;
        this.versions = new VersionData(templateView, viewModel);
    }

    @Override
    public long getVersion() {
        return Math.max(viewModel.getVersion(),templateView.getVersion());
    }

    @Override
    public boolean isValid() {
        return versions.isValid();
    }

    @Override
    public String get() {
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>");
        sb.append("\n");
        sb.append("# ERROR: ").append(exception.getMessage()).append("\n");
        sb.append("# Template:\n");
        sb.append(templateView.toString()).append("\n");
        sb.append("# View:\n");
        sb.append(viewModel.toString());
        sb.append("\n");
        sb.append("# StackTrace:\n");
        StringWriter errors = new StringWriter();
        this.exception.printStackTrace(new PrintWriter(errors));
        sb.append(errors.toString());
        sb.append("\n</pre>\n");
        return sb.toString();
    }

}
