package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Versioned;
import io.virtdata.docsys.metafs.fs.renderfs.model.ViewModel;

import java.util.function.Function;

/**
 * Renderable content
 */
public interface Renderable extends Versioned, Function<ViewModel,String> {
}
