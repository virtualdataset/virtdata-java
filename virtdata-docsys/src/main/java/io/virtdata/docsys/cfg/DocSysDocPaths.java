package io.virtdata.docsys.cfg;

import io.virtdata.annotations.Service;
import io.virtdata.docsys.api.DocPath;
import io.virtdata.docsys.api.DocPaths;
import io.virtdata.docsys.api.PathDescriptor;
import io.virtdata.util.VirtDataResources;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service(DocPaths.class)
public class DocSysDocPaths implements DocPaths {

    @Override
    public List<PathDescriptor> getPathDescriptors() {
        ArrayList<PathDescriptor> paths = new ArrayList<>();
        Path docpath = VirtDataResources.findPathIn(
                "virtdata-docsys/src/main/resources/docs-for-docsys/",
                "docs-for-docsys/"
        );
        paths.add(new DocPath(docpath, "Docs for DocSys", 1));
        return paths;
    }
}
