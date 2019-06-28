package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

public class StringContent implements RenderedContent {

    private final String renderedText;
    private final long version;

    public StringContent(String renderedText, long version) {
        this.renderedText = renderedText;
        this.version = version;
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
}
