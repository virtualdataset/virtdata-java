package io.virtdata.docsys.metafs.fs.renderfs.api.rendering;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;

import java.util.function.Function;

/**
 * A Renderer is any function which can render a document of some
 * type from a view of the source
 */
public interface Renderer extends Function<RenderingScope, RenderedContent>, Versioned {
    String wrapError(String error);
}
