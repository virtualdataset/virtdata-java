package io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

public class VirtualFileAttributeMap extends HashMap<String,Object> {

    private final IntSupplier sizereader;
    private LongSupplier versionreader;

    public VirtualFileAttributeMap(
            Path sourcePath,
            Map<String, Object> sourceAttrs,
            Path path,
            IntSupplier sizereader,
            LongSupplier versionreader
    ) {
        this.versionreader = versionreader;
        putAll(sourceAttrs);
        remove("size");
        remove("lastModifiedTime");
        this.sizereader = sizereader;
    }

    @Override
    public Object get(Object key) {
        if ("size".equals(key)) {
            return sizereader.getAsInt();
        } else if ("lastModifiedTime".equals(key)) {
            return versionreader.getAsLong();
        }
        return super.get(key);
    }
}
