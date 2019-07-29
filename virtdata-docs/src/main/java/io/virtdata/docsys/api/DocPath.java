package io.virtdata.docsys.api;

import java.nio.file.Path;

public class DocPath implements PathDescriptor {

    private final Path of;
    private final String descriptiveName;
    private int priority;

    public static DocPath from(Path path, String descriptiveName, int priority) {
        return new DocPath(path, descriptiveName,priority);
    }

    public DocPath(Path of, String descriptiveName, int priority) {

        this.of = of;
        this.descriptiveName = descriptiveName;
        this.priority = priority;
    }

    public Path getPath() {
        return of;
    }

    public String getName() {
        return descriptiveName;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(PathDescriptor o) {
        int diff = Integer.compare(this.getPriority(), o.getPriority());
        if (diff!=0) return diff;
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "DocPath{" +
                "of=" + of +
                ", descriptiveName='" + descriptiveName + '\'' +
                ", priority=" + priority +
                '}';
    }
}
