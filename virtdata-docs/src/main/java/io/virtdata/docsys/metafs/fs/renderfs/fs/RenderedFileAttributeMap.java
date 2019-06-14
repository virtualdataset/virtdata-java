package io.virtdata.docsys.metafs.fs.renderfs.fs;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class RenderedFileAttributeMap extends HashMap<String,Object> {

    public RenderedFileAttributeMap(Path sourcePath, Map<String, Object> sourceAttrs, Path path, int size) {
        putAll(sourceAttrs);
        put("size",size);
    }
}
