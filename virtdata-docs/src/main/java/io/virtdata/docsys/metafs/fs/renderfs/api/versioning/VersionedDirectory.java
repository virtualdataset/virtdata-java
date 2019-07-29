package io.virtdata.docsys.metafs.fs.renderfs.api.versioning;

import io.virtdata.docsys.metafs.fs.renderfs.api.RendererIO;

import java.nio.file.Files;
import java.nio.file.Path;

public class VersionedDirectory implements Versioned {
    private final long createdVersion;
    private Path sourcePath;

    public VersionedDirectory(Path sourcePath) {
        if (!Files.isDirectory(sourcePath)) {
            this.sourcePath = sourcePath.getParent();
        } else {
            this.sourcePath = sourcePath;
        }
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
