package io.virtdata.docsys.api;

import java.nio.file.Path;
import java.util.*;

public class DocNameSpaceImpl implements DocNameSpace {

    private final Set<Path> paths = new HashSet<>();
    private String namespace;
    private boolean enabledByDefault = false;

    public DocNameSpaceImpl() {}

    public static DocNameSpaceImpl of(String descriptiveName, Path path) {
        return new DocNameSpaceImpl().setNameSpace(descriptiveName).addPath(path);
    }

    private DocNameSpaceImpl setNameSpace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public DocNameSpaceImpl(String name) {
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
    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    @Override
    public String toString() {
        return "DocPath{" +
                "namespace='" + namespace + '\'' +
                ",paths=" + paths.toString() +
                '}';
    }

    public DocNameSpaceImpl addPath(Path path) {
        this.paths.add(path);
        return this;
    }

    public DocNameSpaceImpl enabledByDefault() {
        this.enabledByDefault=true;
        return this;
    }

    @Override
    public Iterator<Path> iterator() {
        return this.paths.iterator();
    }

    public DocNameSpaceImpl setEnabledByDefault(boolean enabledByDefault) {
        this.enabledByDefault=enabledByDefault;
        return this;
    }
}
