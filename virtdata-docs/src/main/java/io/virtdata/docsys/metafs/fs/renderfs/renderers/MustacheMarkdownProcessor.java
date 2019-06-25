package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.virtdata.docsys.metafs.fs.renderfs.api.MarkdownStringer;
import io.virtdata.docsys.metafs.fs.renderfs.api.Renderer;
import io.virtdata.docsys.metafs.fs.renderfs.api.TemplateCompiler;
import io.virtdata.docsys.metafs.fs.renderfs.model.TargetPathView;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("ALL")
public class MustacheMarkdownProcessor implements TemplateCompiler {

    public final static Mustache.Compiler compiler = Mustache.compiler().withFormatter(
            new Formatter()
    );

    @Override
    public Renderer apply(ByteBuffer byteBuffer) {
        return new MustacheRenderer(byteBuffer);
    }

    @Override
    public String toString() {
        return MustacheMarkdownProcessor.class.getSimpleName();
    }

    public static class MustacheRenderer implements Renderer {

        private String rawTemplate;
        private Template compiledTemplate;

        public MustacheRenderer(ByteBuffer templateBuffer) {
            rawTemplate = new String(templateBuffer.array(), StandardCharsets.UTF_8);
        }

        @Override
        public ByteBuffer apply(TargetPathView targetPathView) {
            try {

                if (compiledTemplate == null) {
                    this.compiledTemplate = compiler.compile(rawTemplate);
                }

                String renderedText = compiledTemplate.execute(targetPathView);
                return ByteBuffer.wrap(renderedText.getBytes(StandardCharsets.UTF_8));
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder(rawTemplate.length() + 4096);
                sb.append(e.getMessage());
                return ByteBuffer.wrap(sb.toString().getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    public static class Formatter implements Mustache.Formatter {

        @Override
        public String format(Object value) {
            if (value instanceof MarkdownStringer) {
                return ((MarkdownStringer) value).asMarkdown();
            } else {
                return value.toString();
            }
        }
    }
}


