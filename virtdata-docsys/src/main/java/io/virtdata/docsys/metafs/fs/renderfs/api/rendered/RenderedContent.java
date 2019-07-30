package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.Versioned;

import java.util.function.Supplier;

public interface RenderedContent<T> extends Versioned, Supplier<T> {

}
