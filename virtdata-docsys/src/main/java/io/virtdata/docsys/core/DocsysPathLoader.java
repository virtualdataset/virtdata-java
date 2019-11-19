package io.virtdata.docsys.core;

import io.virtdata.docsys.api.DocPathInfo;
import io.virtdata.docsys.api.DocsInfo;
import io.virtdata.docsys.api.DocsysDynamicManifest;
import io.virtdata.docsys.api.DocsPath;

import java.nio.file.Path;
import java.util.*;

/**
 * The standard way to load and use all of the {@link DocsPath}
 * instances which are present in the runtime via SPI.
 *
 * This implementation ensures that names space collisions are known.
 */
public class DocPathLoader {

    public static Map<String, Set<Path>> load() {
        Map<String, Set<Path>> docs = new HashMap<>();
        ServiceLoader<DocsysDynamicManifest> loader = ServiceLoader.load(DocsysDynamicManifest.class);
        for (DocsysDynamicManifest mf : loader) {
            DocsInfo docsInfo = mf.getDocsInfo();
            for (DocPathInfo info : docsInfo.getInfo()) {

                Set<Path> paths = docs.computeIfAbsent(
                        info.getNameSpace(),
                        s -> new HashSet<>()
                );
                for (Path path : info.getPaths()) {
                    if (paths.contains(path)) {
                        throw new RuntimeException("namespace " + info.getNameSpace() +
                                " already has a path " + path.toString());
                    }
                    paths.add(path);
                }
            }

        }
        return docs;

    }
}
