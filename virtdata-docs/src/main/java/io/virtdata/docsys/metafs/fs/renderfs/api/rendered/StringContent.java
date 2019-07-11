package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Versioned;

import java.util.function.Supplier;

public class StringContent implements RenderedContent {

    private final Supplier<String> renderedText;
    private final long version;
    private Versioned dependency;

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
        return renderedText.get();
    }

    public String toString() {
        return get();
    }

    @Override
    public Versioned getVersionDependency() {
        return dependency;
    }
}
