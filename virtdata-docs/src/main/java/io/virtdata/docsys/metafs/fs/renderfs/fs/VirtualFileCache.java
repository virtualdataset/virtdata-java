package io.virtdata.docsys.metafs.fs.renderfs.fs;

import io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio.VirtualFile;
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
        logger.info("REQUESTFOR " + key);
        try {
            VirtualFile vf = cacheMap.get(key);
            if (vf!=null) {
                if (!devmode && vf.getRenderedContent().isCurrent()) {
                    logger.trace("REUSED  " + key);
                    return vf;
                } else {
                    logger.info("REFRESH " + (devmode ? "(DEVMODE)" : "") + key);
                    vf=mappingFunction.apply(key);
                    if (vf==null) {
                        logger.info("NULLREN " + key);
                    } else {
                        logger.info("PRESENT " + key);
                        cacheMap.put(key,vf);
                    }
                }
            }
            else {
                logger.info("COMPUTE " + key);
                vf = mappingFunction.apply(key);
                if (vf==null) {
                    logger.info("NULLREN " + key);
                } else {
                    logger.info("PRESENT " + key);
                    cacheMap.put(key,vf);
                }
            }
            return vf;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
