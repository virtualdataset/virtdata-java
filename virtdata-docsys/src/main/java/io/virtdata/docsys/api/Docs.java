package io.virtdata.docsys.api;

import io.virtdata.util.VirtDataResources;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Docs implements DocsBinder {

    private LinkedList<DocsNameSpaceImpl> namespaces = new LinkedList<>();

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



    private Docs addNamespace(String name) {
        namespaces.add(new DocsNameSpaceImpl(name));
        return this;
    }

    @Override
    public DocsBinder merge(DocsBinder other) {
        for (DocsNameSpace namespace : other.getNamespaces()) {
            this.namespace(namespace.getName());
            setEnabledByDefault(namespace.isEnabledByDefault());
            for (Path path : namespace.getPaths()) {
                addPath(path);
            }
        }
        return this.asDocsInfo();
    }

    @Override
    public DocsBinder merge(DocsNameSpace namespace) {
        this.namespace(namespace.getName());
        setEnabledByDefault(namespace.isEnabledByDefault());
        for (Path path : namespace) {
            this.addPath(path);
        }
        return this.asDocsInfo();
    }

    @Override
    public List<Path> getPaths() {
        List<Path> paths = new ArrayList<>();
        for (DocsNameSpaceImpl ns : this.namespaces) {
            paths.addAll(ns.getPaths());
        }
        return paths;
    }

    @Override
    public Map<String, Set<Path>> getPathMap() {
        Map<String, Set<Path>> pm = new HashMap();
        for (DocsNameSpaceImpl ns : this.namespaces) {
            pm.put(ns.getName(), new HashSet<>(ns.getPaths()));
        }
        return pm;
    }

    @Override
    public List<DocsNameSpace> getNamespaces() {
        return new LinkedList<>(this.namespaces);
    }

    @Override
    public Iterator<DocsNameSpace> iterator() {
        List<DocsNameSpace> pathinfos = new ArrayList<>(this.namespaces);
        return pathinfos.iterator();
    }

    public Map<String, Set<Path>> getPathMaps() {
        Map<String, Set<Path>> maps = new HashMap<>();
        for (DocsNameSpaceImpl namespace : namespaces) {
            Set<Path> paths = new HashSet<>();
            namespace.forEach(paths::add);
            maps.put(namespace.getName(), paths);
        }

        return maps;
    }

    public DocsBinder asDocsInfo() {
        return this;
    }

    @Override
    public DocsBinder remove(Set<String> namespaces) {
        Docs removed = new Docs();
        ListIterator<DocsNameSpaceImpl> iter = this.namespaces.listIterator();
        while (iter.hasNext()) {
            DocsNameSpaceImpl next = iter.next();
            if (namespaces.contains(next.getName())) {
                iter.previous();
                iter.remove();
                removed.merge(next);
            }
        }
        return removed;
    }


}
