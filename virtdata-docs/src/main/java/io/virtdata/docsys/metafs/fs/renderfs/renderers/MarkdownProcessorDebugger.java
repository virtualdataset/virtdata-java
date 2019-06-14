package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import io.virtdata.docsys.metafs.fs.renderfs.api.Renderer;
import io.virtdata.docsys.metafs.fs.renderfs.api.TemplateCompiler;
import io.virtdata.docsys.metafs.fs.renderfs.model.TargetPathView;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MarkdownProcessorDebugger implements TemplateCompiler {

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
            BasedSequence[] segments = document.getSegments();
            StringBuilder sb = new StringBuilder();
            for (BasedSequence segment : segments) {
                sb.append("segment:\n").append(segment.toString());
            }

            String rendered = sb.toString();
            byte[] bytes = rendered.getBytes(StandardCharsets.UTF_8);
            ByteBuffer wrapped = ByteBuffer.wrap(bytes);
            return wrapped;
        }
    }

    @Override
    public String toString() {
        return MarkdownProcessorDebugger.class.getSimpleName();
    }
}
