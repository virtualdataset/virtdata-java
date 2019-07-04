package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendering.Versioned;

public class StringContent implements RenderedContent {

    private final String renderedText;
    private final long version;
    private Versioned dependency;

    public StringContent(String renderedText, long version, Versioned dependency) {
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
        return renderedText;
    }

    public String toString() {
        return renderedText.toString();
    }

    @Override
    public Versioned getVersionDependency() {
        return dependency;
    }
}
