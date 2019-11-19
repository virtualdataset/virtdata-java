package io.virtdata.docsys.api;

import java.nio.file.Path;

public class DocRoot implements DocPathInfo {

    private final Path of;
    private final String descriptiveName;

    public static DocRoot of(Path path, String descriptiveName) {
        return new DocRoot(path, descriptiveName);
    }

    public DocRoot(Path of, String descriptiveName) {

        this.of = of;
        this.descriptiveName = descriptiveName;
    }

    public Path getPath() {
        return of;
    }

    public String getNameSpace() {
        return descriptiveName;
    }

    @Override
    public int compareTo(DocPathInfo o) {
        int diff = getNameSpace().compareTo(o.getNameSpace());
        if (diff!=0) {
            return diff;
        }
        return getPath().compareTo(o.getPath());
    }

    @Override
    public String toString() {
        return "DocPath{" +
                "of=" + of +
                ", descriptiveName='" + descriptiveName + '\'' +
                '}';
    }
}
