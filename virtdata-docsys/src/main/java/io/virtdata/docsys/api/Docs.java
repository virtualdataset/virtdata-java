package io.virtdata.docsys.api;

import io.virtdata.util.VirtDataResources;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Docs implements DocsInfo {

    private LinkedList<DocsPath> namespaces = new LinkedList<>();

    public Docs() {
    }

    public Docs namespace(String namespace) {
        return addNamespace(namespace);
    }

    public Docs addFirstFoundPath(String... potentials) {
        Path pathIn = VirtDataResources.findPathIn(potentials);
        if (pathIn==null || !Files.exists(pathIn)) {
            throw new RuntimeException("Unable to find a path in one of " + Arrays.stream(potentials).collect(Collectors.joining(",")));
        }
        return this.addPath(pathIn);
    }

    public Docs addPath(Path path) {
        if (namespaces.peekLast()==null) {
            throw new RuntimeException("You must add a namespace first.");
        }
        namespaces.peekLast().addPath(path);
        return this;

    }

    private Docs addNamespace(String namespace) {
        namespaces.add(new DocsPath(namespace));
        return this;
    }

    @Override
    public DocsInfo merge(DocsInfo other) {
        for (DocPathInfo docPathInfo : other) {
            namespace(docPathInfo.getNameSpace());
            for (Path path : docPathInfo) {
                addPath(path);
            }
        }
        return this.asDocsInfo();
    }

    @Override
    public DocsInfo merge(DocPathInfo pathInfo) {
        this.namespace(pathInfo.getNameSpace());
        for (Path path : pathInfo) {
            this.addPath(path);
        }
        return this.asDocsInfo();
    }

    @Override
    public List<Path> getPaths() {
        List<Path> paths = new ArrayList<>();
        for (DocsPath ns : this.namespaces) {
            paths.addAll(ns.getPaths());
        }
        return paths;
    }

    @Override
    public Map<String, Set<Path>> getPathMap() {
        Map<String,Set<Path>> pm = new HashMap();
        for (DocsPath ns : this.namespaces) {
            pm.put(ns.getNameSpace(),new HashSet<>(ns.getPaths()));
        }
        return pm;
    }

    @Override
    public Iterator<DocPathInfo> iterator() {
        List<DocPathInfo> pathinfos = new ArrayList<>(this.namespaces);
        return pathinfos.iterator();
    }

    public Map<String, Set<Path>> getPathMaps() {
        Map<String,Set<Path>> maps = new HashMap<>();
        for (DocsPath namespace : namespaces) {
            Set<Path> paths = new HashSet<>();
            namespace.forEach(paths::add);
            maps.put(namespace.getNameSpace(),paths);
        }

        return maps;
    }

    public DocsInfo asDocsInfo() {
        return (DocsInfo) this;
    }

    @Override
    public DocsInfo remove(Set<String> namespaces) {
        Docs removed = new Docs();
        ListIterator<DocsPath> iter = this.namespaces.listIterator();
        while (iter.hasNext()) {
            DocsPath next = iter.next();
            if (namespaces.contains(next.getNameSpace())) {
                iter.previous();
                iter.remove();
                removed.merge(next);
            }
        }
        return removed;
    }


}
