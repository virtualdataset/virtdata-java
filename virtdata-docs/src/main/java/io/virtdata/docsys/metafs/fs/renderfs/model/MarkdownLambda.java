package io.virtdata.docsys.metafs.fs.renderfs.model;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.virtdata.docsys.metafs.fs.renderfs.api.Renderer;
import io.virtdata.docsys.metafs.fs.renderfs.renderers.MarkdownProcessor;

import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class MarkdownLambda implements Mustache.Lambda {

    private final TargetPathView view;

    public MarkdownLambda(TargetPathView v) {
        this.view = v;
    }

    @Override
    public void execute(Template.Fragment frag, Writer out) throws IOException {
        String minitmpl = frag.execute(view);
        ByteBuffer minitplbuf = ByteBuffer.wrap(minitmpl.getBytes());
        Renderer proc = new MarkdownProcessor().apply(minitplbuf);
        ByteBuffer rendered = proc.apply(view);
        String str = StandardCharsets.UTF_8.decode(rendered).toString();
        out.write(str);
    }
}
