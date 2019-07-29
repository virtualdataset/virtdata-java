package io.virtdata.docsys.metafs.fs.renderfs.api.versioning;

import io.virtdata.docsys.metafs.fs.renderfs.api.RendererIO;

import java.nio.file.Path;

public class VersionedPath implements Versioned {
    private final long createdVersion;
    private Path sourcePath;

    public VersionedPath(Path sourcePath) {
        this.sourcePath = sourcePath;
        this.createdVersion = getVersion();
    }

    @Override
    public long getVersion() {
        return RendererIO.mtimeFor(sourcePath);
    }

    @Override
    public boolean isValid() {
        return createdVersion>=getVersion();
    }
}
