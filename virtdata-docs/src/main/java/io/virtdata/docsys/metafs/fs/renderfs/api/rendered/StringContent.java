package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Versioned;

import java.util.function.Supplier;

public class StringContent implements RenderedContent {

    private final Supplier<String> renderedText;
    private final long version;
    private Versioned dependency;
    private String cache;

    public StringContent(Supplier<String> renderedText, long version, Versioned dependency) {
        this.renderedText = renderedText;
        this.version = version;
        this.dependency = dependency;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public String get() {
        if (cache==null) {
            cache = renderedText.get();
        }
        return cache;
    }

    public String toString() {
        throw new RuntimeException("not here");
    }

    @Override
    public Versioned getVersionDependency() {
        return dependency;
    }
}
