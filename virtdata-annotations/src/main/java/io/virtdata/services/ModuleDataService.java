package io.virtdata.services;

import java.io.InputStream;
import java.nio.file.Path;

public interface ModuleDataService {

    /**
     * When a JPMS module is searched for resources, they must be in
     * within this directory in the module. This allows files to be
     * bundled with a module in its distinct namespace without requiring
     * package level encapsulation.
     */
    public final static Path DATAPATH = Path.of("MODULE-DATA");

    /**
     * @deprecated This will no longer be used as a default search path within modules
     */
    public final static Path DEPRECATED_PATH = Path.of("data");

    InputStream getInputStream(Path dataPath, Path... searchPaths);

}
