package io.virtdata.docsys.metafs.fs.renderfs.fs;

import io.virtdata.docsys.metafs.fs.renderfs.api.rendered.RenderedContent;
import io.virtdata.docsys.metafs.fs.renderfs.fs.virtualio.VirtualFile;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.function.Function;

public class VirtualFileCache extends HashMap<Path, VirtualFile>{

    @Override
    public synchronized VirtualFile computeIfAbsent(
            Path key, Function<? super Path, ? extends VirtualFile> mappingFunction
    ) {

        if (containsKey(key)) {
            VirtualFile virtualFile = get(key);
            RenderedContent renderedContent = virtualFile.getRenderedContent();
            if (!renderedContent.isCurrent()) {
                remove(key);
            }
        }
        return super.computeIfAbsent(key, mappingFunction);
    }
}
