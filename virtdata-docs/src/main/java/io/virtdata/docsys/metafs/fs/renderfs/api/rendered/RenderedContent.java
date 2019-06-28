package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Versioned;

import java.util.function.Supplier;

public interface RenderedContent extends Supplier<String>, Versioned {
}
