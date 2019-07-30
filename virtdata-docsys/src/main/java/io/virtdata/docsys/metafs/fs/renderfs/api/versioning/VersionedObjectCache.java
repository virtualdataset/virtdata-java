package io.virtdata.docsys.metafs.fs.renderfs.api.versioning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class VersionedObjectCache {
    public final static Logger logger = LoggerFactory.getLogger(VersionedObjectCache.class);

    public static VersionedObjectCache INSTANCE = new VersionedObjectCache();
    private ConcurrentHashMap<String, Versioned> cache = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Class<? extends Versioned>, ConcurrentHashMap<String, Versioned>> typeMap = new ConcurrentHashMap<>();

    private VersionedObjectCache() {
    }

    public <T extends Versioned> T getOrCreate(
            Class<T> clazz,
            String key,
            Supplier<T> supplier
    ) {
        ConcurrentHashMap<String, Versioned> typedMap = typeMap.computeIfAbsent(clazz, (Class<?> c) -> new ConcurrentHashMap<>());

        Versioned versioned = typedMap.get(key);
        if (versioned == null || !versioned.isValid()) {
            versioned = supplier.get();
        }
        if (typedMap.containsKey(key)) {
            typedMap.put(key, versioned);
        }
        return clazz.cast(versioned);
    }

}
