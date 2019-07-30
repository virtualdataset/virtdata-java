package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.Versioned;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;

import java.util.function.Function;

/**
 * Renderable content is versioned, and provides a means to transform
 * a view model into a UTF-8 String.
 */
public interface Renderable extends Versioned, Function<ViewModel,String> {
}
