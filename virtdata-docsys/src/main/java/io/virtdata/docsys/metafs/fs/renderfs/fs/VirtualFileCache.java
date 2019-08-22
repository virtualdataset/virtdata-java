package io.virtdata.docsys.metafs.fs.renderfs.fs;

import io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio.VirtualFile;
import io.virtdata.docsys.metafs.fs.virtual.DebugHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class VirtualFileCache {
    private final static Logger logger = LoggerFactory.getLogger(VirtualFileCache.class);

    private final Map<Path, VirtualFile> cacheMap = new HashMap<>();
    private boolean devmode = false;


    public synchronized VirtualFile computeIfAbsent(
            Path key, Function<? super Path, ? extends VirtualFile> mappingFunction
    ) {
        if (logger.isTraceEnabled()) {
            String caller = DebugHelper.matchesCallerTree(Thread.currentThread().getStackTrace(), "org.eclipse.jetty.*");
            if (caller != null) {
                logger.trace("REQUESTFOR " + key);
                logger.trace("BY " + caller);
            }
        }
        try {

            VirtualFile vf = cacheMap.get(key);
            boolean compute = false;

            if (vf == null) {
                compute = true;
            } else if (!vf.isValid()) {
                logger.debug("RECOMPUTING >" + key);
                compute = true;
            } else if (devmode) {
                logger.trace("FORCED >" + key);
                compute = true;
            }

            if (compute) {
                vf = mappingFunction.apply(key);
                cacheMap.put(key,vf);
                logger.debug("COMPUTED " + key);
            }

            return vf;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
