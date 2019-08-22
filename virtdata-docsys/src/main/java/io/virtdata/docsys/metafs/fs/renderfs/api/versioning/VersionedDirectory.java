package io.virtdata.docsys.metafs.fs.renderfs.api.versioning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

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
        try {
            return Files.getLastModifiedTime(sourcePath).to(TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isValid() {
        return createdVersion>=getVersion();
    }
}
