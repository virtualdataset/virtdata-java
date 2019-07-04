package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.ExceptionContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.StringContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Renderer;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.RenderingScope;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateCompiler;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;

public class MarkdownProcessor implements TemplateCompiler {

    @Override
    public Renderer apply(TemplateView templateView) {
        return new MarkdownRenderer(templateView.getRawTemplate());
    }

    public static class MarkdownRenderer implements Renderer {
        protected final static Parser parser = Parser.builder().build();
        protected final static HtmlRenderer renderer = HtmlRenderer.builder().build();

        private final Document document;
        private long version;

        public MarkdownRenderer(String template) {
            document = parser.parse(template);
        }

        @Override
        public RenderedContent apply(RenderingScope scope) {
            try {
                String rendered = renderer.render(document);
                this.version = scope.getVersion();
                return new StringContent(rendered, scope.getVersion(), scope);
            } catch (Exception e) {
                return new ExceptionContent(e, scope.getVersion(), scope);
            }
        }

        @Override
        public long getVersion() {
            return this.version;
        }

        @Override
        public String wrapError(String error) {
            return "\n```\nMarkdown Error:\n" + error + "\n```\n";
        }
    }

    @Override
    public String toString() {
        return "\uD83C\uDD6B";
//        return MarkdownProcessor.class.getSimpleName();
    }
}
