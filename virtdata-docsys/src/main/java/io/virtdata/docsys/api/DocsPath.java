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

    //    @Override
//    public int compareTo(DocPathInfo o) {
//        int diff = getNameSpace().compareTo(o.getNameSpace());
//        if (diff!=0) {
//            return diff;
//        }
//
//        ArrayList<Path> thisp = new ArrayList<>(getPaths());
//        thisp.sort(Comparator.comparing(Path::toString));
//        Iterator<Path> thisi = thisp.iterator();
//        ArrayList<Path> thatp = new ArrayList<>(o.getPaths());
//        thatp.sort(Comparator.comparing(Path::toString));
//        Iterator<Path> thati = thatp.iterator();
//
//        while (thisi.hasNext() && thati.hasNext()) {
//            int cmp = thisi.next().compareTo(thati.next());
//            if (cmp!=0) {
//                return cmp;
//            }
//        }
//        if (thisi.hasNext()) return -1;
//        if (thati.hasNext()) return 1;
//        return 0;
//
//    }
//
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
