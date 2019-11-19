package io.virtdata.docsys.api;

import java.nio.file.Path;

public interface DocPathInfo extends Comparable<DocPathInfo> {
    /**
     * A simple name which can be used to nest the enclosed path within a larger
     * namespace. Users of this interface should never host content from the path
     * at a root level separate from the namespace.
     * Further, when multiple descriptors are present at some level, names which
     * are duplicated should cause an error to be thrown.
     * @return A canonical namespace identifier
     */
    public String getNamespace();

    /**
     * The content root of the files contained within this doc path.
     * @return A readable Path
     */
    public Path getRootPath();
}
