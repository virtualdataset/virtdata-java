package io.virtdata.docsys;

import io.virtdata.annotations.Service;
import io.virtdata.docsys.api.Docs;
import io.virtdata.docsys.api.DocsInfo;
import io.virtdata.docsys.api.DocsysStaticManifest;

@Service(DocsysStaticManifest.class)
public class DocsysAppPath implements DocsysStaticManifest {

    public DocsInfo getDocsInfo() {
        return new Docs().namespace("virtdata").addFirstFoundPath(
                "virtdata-docsys/src/main/resources/docs-for-docsys/",
                "docs-for-docsys/");
    }

}
