package io.virtdata.docsys.api;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DocsInfo extends Iterable<DocPathInfo> {
    void merge(DocsInfo other);
    List<Path> getPaths();
    Map<String, Set<Path>> getPathMap();
}
