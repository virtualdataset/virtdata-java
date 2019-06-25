package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import io.virtdata.docsys.metafs.fs.renderfs.api.Renderer;
import io.virtdata.docsys.metafs.fs.renderfs.api.TemplateCompiler;
import io.virtdata.docsys.metafs.fs.renderfs.model.TargetPathView;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MarkdownProcessor implements TemplateCompiler {

    @Override
    public Renderer apply(ByteBuffer byteBuffer) {
        return new MarkdownRenderer(byteBuffer);
    }

    public static class MarkdownRenderer implements Renderer {
        protected final static Parser parser = Parser.builder().build();
        protected final static HtmlRenderer renderer = HtmlRenderer.builder().build();

        private final Document document;

        public MarkdownRenderer(ByteBuffer byteBuffer) {
            document = parser.parse(new String(byteBuffer.array(), StandardCharsets.UTF_8));
        }

        @Override
        public ByteBuffer apply(TargetPathView targetPathView) {
            String rendered = renderer.render(document);
            byte[] bytes = rendered.getBytes(StandardCharsets.UTF_8);
            ByteBuffer wrapped = ByteBuffer.wrap(bytes).asReadOnlyBuffer();
            return wrapped;
        }
    }

    @Override
    public String toString() {
        return "\uD83C\uDD6B";
//        return MarkdownProcessor.class.getSimpleName();
    }
}
