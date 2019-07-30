package io.virtdata.docsys.metafs.fs.renderfs.model.topics;

import com.vladsch.flexmark.ast.Heading;

import java.nio.file.Path;

public class HeaderTopic extends PathTopic {

    private final Heading heading;

    public HeaderTopic(Heading heading, Path p) {
        super(p);
        this.heading = heading;
        setLevel(heading.getLevel());
    }

    @Override
    public void setLevel(int level) {
        super.setLevel(level);
        heading.setLevel(level);
    }

    @Override
    public String getName() {
        return heading.getText().toString();
    }

    @Override
    public int getLevel() {
        return heading.getLevel();
    }

    @Override
    public String getPath() {
        String p = path.toString();
        int extensionAt = p.lastIndexOf(".");
        return p.substring(0,extensionAt);
    }

    @Override
    public String toString() {
        return "[HEADER] " + super.toString();
    }
}
