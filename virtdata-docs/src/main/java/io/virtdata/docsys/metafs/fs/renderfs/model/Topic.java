package io.virtdata.docsys.metafs.fs.renderfs.model;

import java.nio.file.Path;

public class Topic {
    private final Path path;
    private String name;

    public Topic(String name, Path path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public Path getMdpath() {
        return path;
    }

    public String getPath() {
        return path.toString().replaceAll(".md$","");
    }

    public String toString() {
        return "Topic name:'" + name + "' => path:'" + path.toString() + "'";
    }
}
