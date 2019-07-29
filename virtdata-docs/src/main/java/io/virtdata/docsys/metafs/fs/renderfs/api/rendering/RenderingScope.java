package io.virtdata.docsys.metafs.fs.renderfs.api.rendering;

import io.virtdata.docsys.metafs.fs.renderfs.api.SourcePathTemplate;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.ExceptionContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.VersionData;
import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.Versioned;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class RenderingScope implements Versioned {

    private final static Logger logger = LoggerFactory.getLogger(RenderingScope.class);

    private final TemplateView templateView;
    private final ViewModel viewModel;
    private final TemplateCompiler compiler;
    private final VersionData versions;

    private RenderingScope innerScope;


    private Renderer renderer;

    public RenderingScope(TemplateView templateView, ViewModel viewModel, TemplateCompiler compiler) {
        this.templateView = templateView;
        this.viewModel = viewModel;
        this.compiler = compiler;
        this.versions = new VersionData(templateView, viewModel);
    }

    public RenderingScope(Path sourcePath, Path targetPath, TemplateCompiler compiler) {
        this(new SourcePathTemplate(sourcePath), new ViewModel(sourcePath, targetPath), compiler);
    }


    public TemplateView getTemplate() {
        return templateView;
    }


    public ViewModel getViewModel() {
        return viewModel;
    }

    public RenderedContent getRendered() {
        try {
            if (innerScope!=null) {
                logger.info("RENDERING INNER " + innerScope.getTemplate().getTemplatePath() + " -> " + innerScope.getViewModel().getTarget());
                innerScope.getRendered();
                this.getViewModel().setInner(innerScope.getViewModel());
            }
            if (this.getViewModel().getRenderedContent()==null || !this.getViewModel().isValid()) {
                if (this.renderer==null || !this.renderer.isValid()) {
                    logger.info("COMPILING OUTER " + getTemplate().getTemplatePath() + " -> " + getViewModel().getTarget());
                    this.renderer =compiler.apply(templateView);
                }
                logger.info("RENDERING OUTER " + getTemplate().getTemplatePath() + " -> " + getViewModel().getTarget());
                RenderedContent renderedContent = renderer.apply(this);
                this.getViewModel().setRenderedContent(renderedContent);
            }
            return this.viewModel.getRenderedContent();
        } catch (Exception e) {
            String wrappedMessage = renderer.wrapError(e.getMessage());
            return new ExceptionContent(viewModel,templateView,e);
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

    public String getDiagnosticSummary() {
        StringBuilder sb = new StringBuilder();
        RenderingScope sc = this;
        while (sc!=null) {
            sb.append(sc.getTemplate().getTemplatePath());
            sb.append(" ");
            sc = sc.innerScope;
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

    public long getVersion() {
        return versions.getVersion();
    }

    @Override
    public boolean isValid() {
        return versions.isValid();
    }
}
