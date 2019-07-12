package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import java.nio.file.Path;

public class FileTopic extends PathTopic {
    public FileTopic(Path path) {
        super(path);
    }

    @Override
    public String toString() {
        return "[FILE] " + super.toString();
    }

}
