package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import io.virtdata.docsys.metafs.fs.renderfs.api.versioning.Versioned;

import java.util.ArrayList;
import java.util.List;

public class HeaderTopicsView extends ArrayList<Topic> implements Versioned {

    private final Versioned versions;

    public HeaderTopicsView(List<Topic> topics, Versioned versions) {
        super(topics);
        this.versions = versions;
    }

    @Override
    public long getVersion() {
        return versions.getVersion();
    }

    @Override
    public boolean isValid() {
        return versions.isValid();
    }
}
