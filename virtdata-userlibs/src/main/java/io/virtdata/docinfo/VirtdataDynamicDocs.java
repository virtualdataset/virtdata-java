package io.virtdata.docinfo;

import io.virtdata.annotations.Service;
import io.virtdata.docsys.api.Docs;
import io.virtdata.docsys.api.DocNameSpacesBinder;
import io.virtdata.docsys.api.DocsysDynamicManifest;

@Service(DocsysDynamicManifest.class)
public class VirtdataDynamicDocs implements DocsysDynamicManifest {

    public DocNameSpacesBinder getDocs() {
        return new Docs().namespace("virtdata-docs").addFirstFoundPath(
                "virtdata-userlibs/src/main/resources/docs-for-virtdata/",
                "docs-for-virtdata/")
                .setEnabledByDefault(false)
                .asDocsInfo();
    }

}
