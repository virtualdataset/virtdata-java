package io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class VirtualFileAttributeMap extends HashMap<String,Object> {

    public VirtualFileAttributeMap(Path sourcePath, Map<String, Object> sourceAttrs, Path path, int size) {
        putAll(sourceAttrs);
        put("size",size);
    }
}
