package io.virtdata.docsys.metafs.fs.renderfs.renderers;

import com.samskivert.mustache.Mustache;
import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.*;

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

}


