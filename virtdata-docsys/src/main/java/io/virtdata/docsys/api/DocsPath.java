package io.virtdata.docsys.api;

import java.nio.file.Path;
import java.util.*;

public class DocsPath implements DocPathInfo {

    private final Set<Path> paths = new HashSet<>();
    private String namespace;

    public DocsPath() {}

    public static DocsPath of(String descriptiveName, Path path) {
        return new DocsPath().setNameSpace(descriptiveName).addPath(path);
    }

    private DocsPath setNameSpace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public DocsPath(String name) {
        this.namespace = name;
    }

    public String getNameSpace() {
        return namespace;
    }

    @Override
    public List<Path> getPaths() {
        return new ArrayList<>(this.paths);
    }

    @Override
    public String toString() {
        return "DocPath{" +
                "namespace='" + namespace + '\'' +
                ",paths=" + paths.toString() +
                '}';
    }

    public DocsPath addPath(Path path) {
        this.paths.add(path);
        return this;
    }

    @Override
    public Iterator<Path> iterator() {
        return this.paths.iterator();
    }
}
