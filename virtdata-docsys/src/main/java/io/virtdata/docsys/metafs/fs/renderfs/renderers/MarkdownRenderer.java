package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.CachedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Renderer;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.RenderingScope;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.TemplateView;

import java.util.Arrays;

public class MarkdownRenderer implements Renderer {

    static final MutableDataHolder OPTIONS = new MutableDataSet()
            .set(HtmlRenderer.GENERATE_HEADER_ID, true)
            .set(HtmlRenderer.RENDER_HEADER_ID, true)
            .set(HtmlRenderer.DO_NOT_RENDER_LINKS, false)
            .set(Parser.EXTENSIONS, Arrays.asList(
                    AnchorLinkExtension.create()
            ))
            .set(AnchorLinkExtension.ANCHORLINKS_SET_ID, true)
            .set(AnchorLinkExtension.ANCHORLINKS_WRAP_TEXT, false)
            .set(AnchorLinkExtension.ANCHORLINKS_ANCHOR_CLASS, "header-anchor");

    protected final static Parser parser = Parser.builder(OPTIONS)
            .build();
    protected final static HtmlRenderer renderer = HtmlRenderer.builder(OPTIONS)
            .htmlIdGeneratorFactory(new DocSysIdGenerator.DocSysIdGeneratorFactory()).build();

    private final Document document;
    private long version;

    public MarkdownRenderer(TemplateView template) {
        document = parser.parse(template.getRawTemplate());
    }

    @Override
    public RenderedContent apply(RenderingScope scope) {
        try {
            this.version = scope.getVersion();
            return new CachedContent<>(scope.getViewModel().getTarget().toString(),()->renderer.render(document), scope);
        } catch (Exception e) {
            throw new RuntimeException(e);
//                return new ExceptionContent(e, scope.getVersion(), scope);
        }
    }

    @Override
    public String wrapError(String error) {
        return "\n```\nMarkdown Error:\n" + error + "\n```\n";
    }

    @Override
    public long getVersion() {
        return 0;
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
