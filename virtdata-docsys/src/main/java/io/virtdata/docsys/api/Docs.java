package io.virtdata.docsys.api;

import io.virtdata.util.VirtDataResources;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Docs implements DocNameSpacesBinder {

    private LinkedList<DocNameSpaceImpl> namespaces = new LinkedList<>();

    public Docs() {
    }

    public Docs namespace(String namespace) {
        return addNamespace(namespace);
    }

    public Docs addFirstFoundPath(String... potentials) {
        Path pathIn = VirtDataResources.findPathIn(potentials);
        if (pathIn == null || !Files.exists(pathIn)) {
            throw new RuntimeException("Unable to find a path in one of " + Arrays.stream(potentials).collect(Collectors.joining(",")));
        }
        return this.addPath(pathIn);
    }

    public Docs addPath(Path path) {
        if (namespaces.peekLast() == null) {
            throw new RuntimeException("You must add a namespace first.");
        }
        namespaces.peekLast().addPath(path);
        return this;
    }

    public Docs setEnabledByDefault(boolean enabledByDefault) {
        if (namespaces.peekLast() == null) {
            throw new RuntimeException("You must add a namespace first.");
        }
        namespaces.peekLast().setEnabledByDefault(enabledByDefault);
        return this;
    }



    private Docs addNamespace(String namespace) {
        namespaces.add(new DocNameSpaceImpl(namespace));
        return this;
    }

    @Override
    public DocNameSpacesBinder merge(DocNameSpacesBinder other) {
        for (DocNameSpaceImpl namespace : other.getNamespaces()) {
            this.namespace(namespace.getNameSpace());
            for (Path path : namespace.getPaths()) {
                addPath(path);
            }
        }
        return this.asDocsInfo();
    }

    @Override
    public DocNameSpacesBinder merge(DocNameSpace pathInfo) {
        this.namespace(pathInfo.getNameSpace());
        this.namespaces.peekLast().setEnabledByDefault(pathInfo.isEnabledByDefault());

        for (Path path : pathInfo) {
            this.addPath(path);
        }
        return this.asDocsInfo();
    }

    @Override
    public List<Path> getPaths() {
        List<Path> paths = new ArrayList<>();
        for (DocNameSpaceImpl ns : this.namespaces) {
            paths.addAll(ns.getPaths());
        }
        return paths;
    }

    @Override
    public Map<String, Set<Path>> getPathMap() {
        Map<String, Set<Path>> pm = new HashMap();
        for (DocNameSpaceImpl ns : this.namespaces) {
            pm.put(ns.getNameSpace(), new HashSet<>(ns.getPaths()));
        }
        return pm;
    }

    @Override
    public List<DocNameSpaceImpl> getNamespaces() {
        return this.namespaces;
    }

    @Override
    public Iterator<DocNameSpace> iterator() {
        List<DocNameSpace> pathinfos = new ArrayList<>(this.namespaces);
        return pathinfos.iterator();
    }

    public Map<String, Set<Path>> getPathMaps() {
        Map<String, Set<Path>> maps = new HashMap<>();
        for (DocNameSpaceImpl namespace : namespaces) {
            Set<Path> paths = new HashSet<>();
            namespace.forEach(paths::add);
            maps.put(namespace.getNameSpace(), paths);
        }

        return maps;
    }

    public DocNameSpacesBinder asDocsInfo() {
        return this;
    }

    public DocNameSpace getLastNameSpace() {
        return this.namespaces.peekLast();
    }

    @Override
    public DocNameSpacesBinder remove(Set<String> namespaces) {
        Docs removed = new Docs();
        ListIterator<DocNameSpaceImpl> iter = this.namespaces.listIterator();
        while (iter.hasNext()) {
            DocNameSpaceImpl next = iter.next();
            if (namespaces.contains(next.getNameSpace())) {
                iter.previous();
                iter.remove();
                removed.merge(next);
            }
        }
        return removed;
    }


}
