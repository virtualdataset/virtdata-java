package io.virtdata.docsys.metafs.fs.renderfs.api.rendering;

import io.virtdata.docsys.metafs.fs.renderfs.api.SourcePathTemplate;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.ExceptionContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;

import java.nio.file.Path;

public class RenderingScope implements IRenderingScope {

    private final TemplateView templateView;
    private final ViewModel viewModel;
    private final TemplateCompiler compiler;

    private Renderer renderer;
    private RenderedContent rendered;

    public RenderingScope(TemplateView templateView, ViewModel viewModel, TemplateCompiler compiler) {
        this.templateView = templateView;
        this.viewModel = viewModel;
        this.compiler = compiler;
    }

    public RenderingScope(Path sourcePath, Path targetPath, TemplateCompiler compiler) {
        this(new SourcePathTemplate(sourcePath), new ViewModel(sourcePath, targetPath), compiler);
    }

    @Override
    public long getVersion() {
        return Math.max(templateView.getVersion(),viewModel.getVersion());
    }

    @Override
    public String getTemplateView() {
        return templateView.get();
    }

    @Override
    public ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public RenderedContent getRendered() {
        try {
            if (this.rendered==null || !this.rendered.isValidFor(this)) {
                if (this.renderer==null || !this.renderer.isValidFor(this)) {
                    this.renderer =compiler.apply(templateView);
                }
                this.rendered = renderer.apply(this);
            }
            return this.rendered;
        } catch (Exception e) {
            return new ExceptionContent(e, templateView.getVersion());
        }
    }

    public RenderingScope addParent(RenderingScope outerScope) {
        this.getViewModel().setRendered(this.getRendered());
        outerScope.getViewModel().setInner(this.viewModel);
        return outerScope;
    }
}
