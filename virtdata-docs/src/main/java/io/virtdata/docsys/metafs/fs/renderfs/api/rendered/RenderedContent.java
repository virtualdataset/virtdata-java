package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Versioned;

import java.util.function.Supplier;

public interface RenderedContent extends Supplier<String>, Versioned {

    Versioned getVersionDependency();

    default boolean isCurrent() {
        Versioned dependency = getVersionDependency();
        if (dependency==null) {
            throw new RuntimeException("Version dependency is null");
        }
        return isValidFor(dependency);
    }
}
