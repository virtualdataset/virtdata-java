package io.virtdata.docsys.api;

import java.nio.file.Path;

public interface PathDescriptor extends Comparable<PathDescriptor> {
    public Path getPath();
    public String getName();
    public int getPriority();
}
