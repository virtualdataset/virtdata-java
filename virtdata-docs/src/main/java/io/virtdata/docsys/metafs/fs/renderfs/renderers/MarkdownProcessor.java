package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.StringContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Renderer;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.RenderingScope;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateCompiler;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;

import java.util.Arrays;

public class MarkdownProcessor implements TemplateCompiler {


    @Override
    public Renderer apply(TemplateView templateView) {
        return new MarkdownRenderer(templateView.getRawTemplate());
    }

    @Override
    public String toString() {
        return "\uD83C\uDD6B";
//        return MarkdownProcessor.class.getSimpleName();
    }

    public static class MarkdownRenderer implements Renderer {

        static final MutableDataHolder OPTIONS = new MutableDataSet()
                .set(HtmlRenderer.GENERATE_HEADER_ID, true)
                .set(HtmlRenderer.DO_NOT_RENDER_LINKS, false)
                .set(Parser.EXTENSIONS, Arrays.asList(
                        AnchorLinkExtension.create()
                ))
                .set(AnchorLinkExtension.ANCHORLINKS_SET_ID, true)
                .set(AnchorLinkExtension.ANCHORLINKS_ANCHOR_CLASS, "header-anchor");

        protected final static Parser parser = Parser.builder(OPTIONS)
                .build();
        protected final static HtmlRenderer renderer = HtmlRenderer.builder(OPTIONS)
                .htmlIdGeneratorFactory(new DocSysIdGenerator.DocSysIdGeneratorFactory()).build();

        private final Document document;
        private long version;

        public MarkdownRenderer(String template) {
            document = parser.parse(template);
        }

        @Override
        public RenderedContent apply(RenderingScope scope) {
            try {
                this.version = scope.getVersion();
                return new StringContent(() -> renderer.render(document), scope.getVersion(), scope);
            } catch (Exception e) {
                throw new RuntimeException(e);
//                return new ExceptionContent(e, scope.getVersion(), scope);
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
}
