package io.virtdata.docsys.metafs.fs.renderfs.api.rendered;

import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.Versioned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class CachedContent<T> implements RenderedContent<T> {
    private final static Logger logger = LoggerFactory.getLogger(CachedContent.class);

    private final Supplier<T> renderer;
    private T cache;
    private Versioned versions;

    public CachedContent(Supplier<T> renderer, Versioned versions) {
        this.renderer = renderer;
        this.versions = versions;
    }

    @Override
    public long getVersion() {
        return versions.getVersion();
    }

    @Override
    public boolean isValid() {
        return versions.isValid();
    }

    @Override
    public T get() {
        if (cache==null) {
            logger.trace("CREATING CONTENT");
            cache=renderer.get();
        }
        return cache;
    }

    @Override
    public String toString() {
        return "content[" + (cache==null ? "NULL" : cache.toString().length()+" bytes")+"]";
    }
}
