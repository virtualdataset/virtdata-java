package io.virtdata.apps;

import io.virtdata.annotations.Service;
import io.virtdata.docsys.api.DocPath;
import io.virtdata.docsys.api.DocPaths;
import io.virtdata.docsys.api.PathDescriptor;
import io.virtdata.util.VirtDataResources;

import java.util.ArrayList;
import java.util.List;

@Service(DocPaths.class)
public class VirtDataDocPaths implements DocPaths {

    @Override
    public List<PathDescriptor> getPathDescriptors() {
        List<PathDescriptor> paths = new ArrayList<>();
        paths.add(DocPath.from(
                VirtDataResources.findPathIn(
                        "virtdata-docs/src/main/resources/docs-virtdata"
                        , "virtdata-docs"), "docs-virtdata", 1));
        return paths;
    }

}
