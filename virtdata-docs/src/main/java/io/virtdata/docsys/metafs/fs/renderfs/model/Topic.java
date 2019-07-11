package io.virtdata.docsys.metafs.fs.renderfs.model;

import com.vladsch.flexmark.ast.Heading;
import io.virtdata.docsys.metafs.fs.renderfs.renderers.DocSysIdGenerator;

import java.nio.file.Path;

public class Topic {

    private final Path path;
    private final Heading heading;
    private final static DocSysIdGenerator generator = new DocSysIdGenerator();

    public Topic(Heading heading, Path p) {
        this.path = p;
        this.heading = heading;
    }

    public String getName() {
        return heading.getText().toString();
    }

    public Path getMdpath() {
        return path;
    }

    public int getLevel() {
        return heading.getLevel();
    }

    /**
     * Return the self-same generated ref id that will be rendered in HTML.
     * The rendered HTML and the parsed model of the markdown use the same
     * generator scheme.
     * @return A simple text based ref id
     */
    public String getId() {
        return generator.generateId(heading.getAnchorRefText());
    }

    public String getText() {
        return heading.getAnchorRefText();
    }

    public String getPath() {
        String p = path.toString();
        int extensionAt = p.lastIndexOf(".");
        return p.substring(0,extensionAt);
    }

    public String getIndent() {
        return " ".repeat(getLevel());
    }
    public String getIndent2() {
        return "  ".repeat(getLevel());
    }

    public String toString() {
        return "Topic name:'" + getName() + "' => path:'" + path.toString() + "'";
    }
}
