package io.virtdata.docsys.metafs.fs.renderfs.api.rendering;

import java.util.function.Function;

/**
 * A template compiler is any type of transform which knows how
 * to create a renderer from an image of bytes.
 */
public interface TemplateCompiler extends Function<TemplateView, Renderer> {
}
