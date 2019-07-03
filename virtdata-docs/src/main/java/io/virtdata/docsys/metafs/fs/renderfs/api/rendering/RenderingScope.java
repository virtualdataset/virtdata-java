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
    private RenderingScope innerScope;

    private Renderer renderer;

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
        long thisVersion = Math.max(templateView.getVersion(), viewModel.getVersion());
        return (innerScope==null) ? thisVersion : Math.max(thisVersion, innerScope.getVersion());
    }

    @Override
    public TemplateView getTemplate() {
        return templateView;
    }


    @Override
    public ViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public RenderedContent getRendered() {
        try {
            if (innerScope!=null) {
                innerScope.getRendered();
                this.getViewModel().setInner(innerScope.getViewModel());
            }
            if (this.getViewModel().getRendered()==null || !this.getViewModel().isValidFor(this)) {
                if (this.renderer==null || !this.renderer.isValidFor(this)) {
                    this.renderer =compiler.apply(templateView);
                }
                RenderedContent rendered = renderer.apply(this);
                this.getViewModel().setRendered(rendered);
            }
            return this.viewModel.getRendered();
        } catch (Exception e) {
            String wrappedMessage = renderer.wrapError(e.getMessage());
            return new ExceptionContent(e, getVersion(), wrappedMessage);
        }
    }

    public RenderingScope wrap(RenderingScope innerScope) {
        this.innerScope=innerScope;
        this.viewModel.setInner(innerScope.getViewModel());
        return this;
//
//        RenderedContent rendered = this.getRendered();
//        this.getViewModel().setRendered(rendered);
//        outerScope.getViewModel().setInner(this.viewModel);
//        outerScope.setInnerScope()
//        return outerScope;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[view:").append(this.getViewModel().toString()).append("]");
        int indent = 0;
        RenderingScope scope = this.innerScope;
        while (scope!=null) {
            sb.append("\n").append(" ".repeat(++indent));
            sb.append(scope.toString());
            scope=scope.innerScope;
        }
        return sb.toString();
    }
}
