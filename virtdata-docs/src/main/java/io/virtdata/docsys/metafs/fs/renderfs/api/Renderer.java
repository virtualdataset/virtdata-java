package io.virtdata.docsys.metafs.fs.renderfs.api;

import io.virtdata.docsys.metafs.fs.renderfs.model.TargetPathView;

import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * A Renderer is any function which can render a document of some
 * type from a view of the source
 */
public interface Renderer extends Function<TargetPathView, ByteBuffer> {
}
