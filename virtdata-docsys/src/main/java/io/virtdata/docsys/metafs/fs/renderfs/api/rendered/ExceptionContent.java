package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionData;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ExceptionContent implements RenderedContent<String> {

    private final ViewModel viewModel;
    private final TemplateView templateView;
    private final Exception e;
    private final Object[] details;
    private final VersionData versions;

    public ExceptionContent(ViewModel viewModel, TemplateView templateView, Exception e, Object... details) {
        this.viewModel = viewModel;
        this.templateView = templateView;
        this.e = e;
        this.details = details;
        this.versions = new VersionData(viewModel,templateView);
    }

    @Override
    public String get() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter printStream = new PrintWriter(bos);
        for (Object detail : this.details) {
            printStream.write(detail.toString());
        }
        printStream.write(e.getMessage());
        printStream.flush();
        String out = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        return out;
    }

    public String toString() {
        return get();
    }

    @Override
    public long getVersion() {
        return versions.getVersion();
    }

    @Override
    public boolean isValid() {
        return versions.isValid();
    }
}
