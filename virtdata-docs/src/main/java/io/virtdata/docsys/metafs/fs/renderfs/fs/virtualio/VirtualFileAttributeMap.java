package io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;

public class VirtualFileAttributeMap extends HashMap<String,Object> {

    private final IntSupplier sizereader;

    public VirtualFileAttributeMap(Path sourcePath, Map<String, Object> sourceAttrs, Path path, IntSupplier sizereader) {
        putAll(sourceAttrs);
        remove("size");
        this.sizereader = sizereader;
    }

    @Override
    public Object get(Object key) {
        if ("size".equals(key)) {
            return sizereader.getAsInt();
        }
        return super.get(key);
    }
}
