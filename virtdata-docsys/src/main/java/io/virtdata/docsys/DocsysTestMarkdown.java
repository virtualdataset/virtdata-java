package io.virtdata.docsys;

import io.virtdata.annotations.Service;
import io.virtdata.docsys.api.Docs;
import io.virtdata.docsys.api.DocsInfo;
import io.virtdata.docsys.api.DocsysStaticManifest;

@Service(DocsysStaticManifest.class)
public class DocsysAppPaths extends Docs implements DocsysStaticManifest {

    public DocsInfo getDocsInfo() {
        return new Docs().namespace("docsys").addFirstFoundPath(
                "virtdata-docsys/src/main/resources/docsys-guidebook/",
                "docsys-guidebook/");
    }

}
