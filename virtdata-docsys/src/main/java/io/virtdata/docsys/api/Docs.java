package io.virtdata.docsys.api;

import io.virtdata.annotations.Service;

import java.util.Arrays;
import java.util.List;

@Service(DocsManifestInfo.class)

public class DocsInfo implements DocsManifestInfo {

    private List<DocPathInfo> infos;

    private DocsInfo(DocPathInfo... infos) {
        this.infos = Arrays.asList(infos);
    }

    public static DocsManifestInfo of(DocPathInfo... infos) {
        return new DocsInfo(infos);
    }

    @Override
    public List<DocPathInfo> getDocsInfo() {
        return infos;
    }
}
