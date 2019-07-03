package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.ExceptionContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.StringContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Renderer;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.RenderingScope;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateCompiler;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MustacheProcessor implements TemplateCompiler {

    public final static Mustache.Compiler compiler = Mustache.compiler();

    @Override
    public String toString() {
        return "︷";
//        return MustacheProcessor.class.getSimpleName();
    }

    @Override
    public Renderer apply(TemplateView templateView) {
        return new MustacheRenderer(templateView);
    }

    public static class MustacheRenderer implements Renderer {

        private final TemplateView templateView;
        private Template compiledTemplate;

        public MustacheRenderer(TemplateView templateView) {
            this.templateView = templateView;
        }

        @Override
        public RenderedContent apply(RenderingScope renderingScope) {
            ViewModel viewModel = null;
            try {

                if (compiledTemplate == null) {
                    this.compiledTemplate = compiler.compile(templateView.getRawTemplate());
                }
                viewModel = renderingScope.getViewModel();
                String renderedText = compiledTemplate.execute(viewModel);
                return new StringContent(renderedText,this.getVersion());
            } catch (Exception e) {
                List<String> details = new ArrayList<>();

                if (viewModel!=null) {
                    details.add("View Model:");
                    details.add(viewModel.toString());
                }

                details.add("Template Path:");
                details.add(templateView.getTemplatePath().toString());

                details.add("Raw Template:");
                details.add(templateView.getRawTemplate());


                return new ExceptionContent(e, getVersion(), details.toArray());
            }
        }

        @Override
        public long getVersion() {
            return templateView.getVersion();
        }

        @Override
        public String wrapError(String error) {
            return "Mustache Error\n" + error;
        }
    }

}


